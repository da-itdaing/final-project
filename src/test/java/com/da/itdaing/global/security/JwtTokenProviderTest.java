package com.da.itdaing.global.security;

import com.da.itdaing.domain.common.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

/**
 * JWT Token Provider 단위 테스트
 */
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(
                "itdaing-secret-key-for-hs256-minimum-256-bits-required-for-security-purposes",
                "itdaing-server",
                86400000L // 24시간
        );
    }

    @Test
    @DisplayName("CONSUMER 역할로 토큰을 생성하고 검증한다")
    void createAndValidateToken_Consumer() {
        // given
        Long userId = 1L;
        String role = UserRole.CONSUMER.toAuthority(); // "ROLE_CONSUMER"

        // when
        String token = jwtTokenProvider.createAccessToken(userId, role);

        // then
        assertThat(token).isNotBlank();

        // 토큰 검증 및 클레임 확인
        Claims claims = jwtTokenProvider.validateAndGetClaims(token);
        assertThat(claims.getSubject()).isEqualTo(String.valueOf(userId));
        assertThat(claims.get("role", String.class)).isEqualTo("ROLE_CONSUMER");
        assertThat(claims.getIssuer()).isEqualTo("itdaing-server");
    }

    @Test
    @DisplayName("SELLER 역할로 토큰을 생성하고 검증한다")
    void createAndValidateToken_Seller() {
        // given
        Long userId = 2L;
        String role = UserRole.SELLER.toAuthority(); // "ROLE_SELLER"

        // when
        String token = jwtTokenProvider.createAccessToken(userId, role);

        // then
        assertThat(token).isNotBlank();

        // 토큰 검증 및 클레임 확인
        Long extractedUserId = jwtTokenProvider.getUserIdFromToken(token);
        String extractedRole = jwtTokenProvider.getRoleFromToken(token);

        assertThat(extractedUserId).isEqualTo(userId);
        assertThat(extractedRole).isEqualTo("ROLE_SELLER");
    }

    @Test
    @DisplayName("ADMIN 역할로 토큰을 생성하고 검증한다")
    void createAndValidateToken_Admin() {
        // given
        Long userId = 3L;
        String role = UserRole.ADMIN.toAuthority(); // "ROLE_ADMIN"

        // when
        String token = jwtTokenProvider.createAccessToken(userId, role);

        // then
        Long extractedUserId = jwtTokenProvider.getUserIdFromToken(token);
        String extractedRole = jwtTokenProvider.getRoleFromToken(token);

        assertThat(extractedUserId).isEqualTo(userId);
        assertThat(extractedRole).isEqualTo("ROLE_ADMIN");
    }

    @Test
    @DisplayName("만료된 토큰은 ExpiredJwtException을 발생시킨다")
    void validateExpiredToken() {
        // given
        JwtTokenProvider expiredTokenProvider = new JwtTokenProvider(
                "itdaing-secret-key-for-hs256-minimum-256-bits-required-for-security-purposes",
                "itdaing-server",
                -1000L // 이미 만료된 시간
        );

        String expiredToken = expiredTokenProvider.createAccessToken(1L, UserRole.CONSUMER.toAuthority());

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateAndGetClaims(expiredToken))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    @DisplayName("잘못된 형식의 토큰은 MalformedJwtException을 발생시킨다")
    void validateMalformedToken() {
        // given
        String malformedToken = "invalid.jwt.token";

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateAndGetClaims(malformedToken))
                .isInstanceOf(MalformedJwtException.class);
    }
}

