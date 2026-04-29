package com.kakaotechcampus.journey_planner.application;

import com.kakaotechcampus.journey_planner.application.auth.AuthService;
import com.kakaotechcampus.journey_planner.application.token.TokenService;
import com.kakaotechcampus.journey_planner.domain.member.Member;
import com.kakaotechcampus.journey_planner.domain.member.repository.MemberRepository;
import com.kakaotechcampus.journey_planner.domain.token.TokenType;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.presentation.auth.dto.request.LoginRequestDto;
import com.kakaotechcampus.journey_planner.presentation.auth.dto.request.SignUpRequestDto;
import com.kakaotechcampus.journey_planner.presentation.auth.dto.response.TokenResponseDto;
import com.kakaotechcampus.journey_planner.support.MemberTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder bCryptPasswordEncoder;

    @Nested
    @DisplayName("회원가입(Sign up) 테스트")
    class SignUpTest {
        @Test
        @DisplayName("성공")
        void signUpSuccess() {
            // given
            SignUpRequestDto requestDto = new SignUpRequestDto("김철수", "010-1234-1234", "test@example.com", "password123", "ENTP");
            Member savedMember = MemberTestBuilder.aMember()
                    .withId(1L)
                    .withName("김철수")
                    .withContact("010-1234-1234")
                    .withEmail("test@example.com")
                    .withPassword("encodedPassword")
                    .withMbti("ENTP").build();
            // mock 설정
            when(memberRepository.existsByEmail(anyString())).thenReturn(false);
            when(memberRepository.save(any(Member.class))).thenReturn(savedMember);
            when(tokenService.generateToken(eq(TokenType.ACCESS), eq(1L))).thenReturn("accessToken");
            when(tokenService.generateToken(eq(TokenType.REFRESH), eq(1L))).thenReturn("refreshToken");
            // when
            TokenResponseDto responseDto = authService.signUp(requestDto);
            // then
            assertThat(responseDto.accessToken()).isEqualTo("accessToken");
            assertThat(responseDto.refreshToken()).isEqualTo("refreshToken");
        }

        @Test
        @DisplayName("실패 - 이미 등록된 이메일")
        void signUpFail_AlreadyRegistered() {
            // given
            SignUpRequestDto requestDto = new SignUpRequestDto("김철수", "010-1234-1234", "test@example.com", "password123", "ENTP");
            when(memberRepository.existsByEmail(anyString())).thenReturn(true);

            // when & then
            BusinessException exception = assertThrows(BusinessException.class, () -> {
                authService.signUp(requestDto);
            });
            assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(exception.getCode()).isEqualTo("ME_002");
            assertThat(exception.getMessage()).isEqualTo("이미 존재하는 멤버입니다.");
        }
    }

    @Nested
    @DisplayName("로그인(login) 테스트")
    class LoginTest {
        @Test
        @DisplayName("로그인 성공")
        void loginSuccess() {
            // given
            LoginRequestDto requestDto = new LoginRequestDto("test@example.com", "password123");
            Member member = MemberTestBuilder.aMember().withId(1L).withEmail("test@example.com").withPassword("encodedPassword").build();

            when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
            when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);
            when(tokenService.generateToken(eq(TokenType.ACCESS), anyLong())).thenReturn("accessToken");
            when(tokenService.generateToken(eq(TokenType.REFRESH), anyLong())).thenReturn("refreshToken");

            // when
            TokenResponseDto responseDto = authService.login(requestDto);

            // then
            assertThat(responseDto.accessToken()).isEqualTo("accessToken");
            assertThat(responseDto.refreshToken()).isEqualTo("refreshToken");
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 회원")
        void loginFail_MemberNotFound() {
            // given
            LoginRequestDto requestDto = new LoginRequestDto("test@example.com", "password123");
            when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());

            // when & then
            BusinessException exception = assertThrows(BusinessException.class, () -> {
                authService.login(requestDto);
            });
            assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(exception.getCode()).isEqualTo("ME_001");
            assertThat(exception.getMessage()).isEqualTo("해당 멤버를 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("실패 - 비밀번호 불일치")
        void loginFail_PasswordMismatch() {
            // given
            LoginRequestDto requestDto = new LoginRequestDto("test@example.com", "password123");
            Member member = MemberTestBuilder.aMember().withId(1L).withEmail("test@example.com").withPassword("encodedPassword").build();

            when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
            when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);

            // when & then
            BusinessException exception = assertThrows(BusinessException.class, () -> {
                authService.login(requestDto);
            });
            assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(exception.getCode()).isEqualTo("AE_001");
            assertThat(exception.getMessage()).isEqualTo("로그인에 실패하였습니다.");
        }
    }

    @Nested
    @DisplayName("토큰 재발급(refresh) 테스트")
    class RefreshTest {
        @Test
        @DisplayName("성공")
        void refreshSuccess() {
            // given
            String refreshToken = "validRefreshToken";
            Member member = MemberTestBuilder.aMember().withId(1L).withEmail("test@example.com").withPassword("encodedPassword").build();

            when(tokenService.getId(eq(TokenType.REFRESH), anyString())).thenReturn(1L);
            when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
            when(tokenService.generateToken(eq(TokenType.ACCESS), anyLong())).thenReturn("newAccessToken");
            when(tokenService.generateToken(eq(TokenType.REFRESH), anyLong())).thenReturn("newRefreshToken");

            // when
            TokenResponseDto responseDto = authService.refresh(refreshToken);

            // then
            assertThat(responseDto.accessToken()).isEqualTo("newAccessToken");
            assertThat(responseDto.refreshToken()).isEqualTo("newRefreshToken");
        }
    }
}
