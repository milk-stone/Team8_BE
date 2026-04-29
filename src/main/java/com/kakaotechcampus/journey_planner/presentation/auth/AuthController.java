package com.kakaotechcampus.journey_planner.presentation.auth;

import com.kakaotechcampus.journey_planner.application.auth.AuthService;
import com.kakaotechcampus.journey_planner.presentation.auth.dto.request.LoginRequestDto;
import com.kakaotechcampus.journey_planner.presentation.auth.dto.request.RefreshRequestDto;
import com.kakaotechcampus.journey_planner.presentation.auth.dto.request.SignUpRequestDto;
import com.kakaotechcampus.journey_planner.presentation.auth.dto.response.TokenResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    private ResponseEntity<TokenResponseDto> signInAndLogin(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(signUpRequestDto));
    }

    @PostMapping("/login")
    private ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequestDto));
    }

    @PostMapping("/refresh")
    private ResponseEntity<TokenResponseDto> refresh(@RequestBody @Valid RefreshRequestDto refreshRequestDto) {
        TokenResponseDto tokenResponseDto = authService.refresh(refreshRequestDto.refreshToken());
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponseDto);
    }
}
