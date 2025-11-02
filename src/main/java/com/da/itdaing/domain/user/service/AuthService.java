package com.da.itdaing.domain.user.service;

import com.da.itdaing.domain.user.UserRepository;
import com.da.itdaing.domain.user.Users;
import com.da.itdaing.domain.user.dto.AuthDto;
import com.da.itdaing.domain.user.exception.AuthException;
import com.da.itdaing.global.error.ErrorCode;
import com.da.itdaing.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 소비자 회원가입
     */
    @Transactional
    public AuthDto.SignupResponse signupConsumer(AuthDto.SignupConsumerRequest request) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 아이디 중복 체크
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new AuthException(ErrorCode.DUPLICATE_LOGIN_ID);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 사용자 저장
        Users user = userRepository.save(request.toEntity(encodedPassword));

        log.info("Consumer signed up: userId={}, email={}", user.getId(), user.getEmail());

        return AuthDto.SignupResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    /**
     * 판매자 회원가입
     */
    @Transactional
    public AuthDto.SignupResponse signupSeller(AuthDto.SignupSellerRequest request) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 아이디 중복 체크
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new AuthException(ErrorCode.DUPLICATE_LOGIN_ID);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 사용자 저장
        Users user = userRepository.save(request.toEntity(encodedPassword));

        log.info("Seller signed up: userId={}, email={}", user.getId(), user.getEmail());

        return AuthDto.SignupResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    /**
     * 로그인
     */
    public AuthDto.LoginResponse login(AuthDto.LoginRequest request) {
        // 이메일로 사용자 조회
        Users user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new AuthException(ErrorCode.INVALID_CREDENTIALS));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException(ErrorCode.INVALID_CREDENTIALS);
        }

        // JWT 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(
                user.getId(),
                user.getRole().toAuthority()
        );

        log.info("User logged in: userId={}, email={}", user.getId(), user.getEmail());

        return AuthDto.LoginResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    /**
     * 사용자 프로필 조회
     */
    public AuthDto.UserProfileResponse getProfile(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(ErrorCode.USER_NOT_FOUND));

        return AuthDto.UserProfileResponse.from(user);
    }
}

