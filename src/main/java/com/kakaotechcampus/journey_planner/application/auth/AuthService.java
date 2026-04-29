package com.kakaotechcampus.journey_planner.application.auth;

import com.kakaotechcampus.journey_planner.application.token.TokenService;
import com.kakaotechcampus.journey_planner.domain.member.Member;
import com.kakaotechcampus.journey_planner.domain.member.repository.MemberRepository;
import com.kakaotechcampus.journey_planner.domain.token.TokenType;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.global.exception.ErrorCode;
import com.kakaotechcampus.journey_planner.presentation.auth.dto.request.LoginRequestDto;
import com.kakaotechcampus.journey_planner.presentation.auth.dto.request.SignUpRequestDto;
import com.kakaotechcampus.journey_planner.presentation.auth.dto.response.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final TokenService tokenService;
    private final PasswordEncoder bCryptPasswordEncoder;

    public TokenResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        String email = signUpRequestDto.email();

        if (memberRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.ALREADY_REGISTERED);
        }

        Member member = signUpRequestDto.toEntity();
        member.encodePassword(bCryptPasswordEncoder);
        Member savedMember = memberRepository.save(member);

        String accessToken = tokenService.generateToken(TokenType.ACCESS, savedMember.getId());
        String refreshToken = tokenService.generateToken(TokenType.REFRESH, savedMember.getId());
        return new TokenResponseDto(accessToken, refreshToken);
    }

    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.email();
        String password = loginRequestDto.password();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.verifyPassword(password, bCryptPasswordEncoder)) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        String accessToken = tokenService.generateToken(TokenType.ACCESS, member.getId());
        String refreshToken = tokenService.generateToken(TokenType.REFRESH, member.getId());
        return new TokenResponseDto(accessToken, refreshToken);
    }

    public TokenResponseDto refresh(String refreshToken) {
        Long id = tokenService.getId(TokenType.REFRESH, refreshToken);
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        String newAccessToken = tokenService.generateToken(TokenType.ACCESS, member.getId());
        String newRefreshToken = tokenService.generateToken(TokenType.REFRESH, member.getId());
        // TODO : Redis에 저장된 기존 리프레시 토큰을 새로운 토큰으로 업데이트
        return new TokenResponseDto(newAccessToken, newRefreshToken);
    }
}
