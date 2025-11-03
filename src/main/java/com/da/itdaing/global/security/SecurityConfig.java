package com.da.itdaing.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security 설정
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthenticationHandler jwtAuthenticationHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS 활성화
                .cors(cors -> {})

                // CSRF 비활성화 (JWT 사용)
                .csrf(AbstractHttpConfigurer::disable)

                // 세션 비활성화 (Stateless)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 인증/인가 예외 처리
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationHandler)
                        .accessDeniedHandler(jwtAuthenticationHandler)
                )

                // 권한 설정
                .authorizeHttpRequests(auth -> auth
                        //  프리플라이트 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 공개 API
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/master/**").permitAll()

                        // Swagger/OpenAPI
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Actuator (선택적 공개)
                        .requestMatchers("/actuator/health").permitAll()

                        // 나머지는 인증 필요
                        .anyRequest().authenticated()
                )

                // JWT 필터 추가
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS 설정
     * - 허용 오리진: localhost:5173, localhost:8081
     * - 허용 메서드: GET, POST, PUT, PATCH, DELETE, OPTIONS
     * - 허용 헤더: 모두 허용
     * - 노출 헤더: Authorization
     * - Credentials: 허용
     * - 적용 경로: /api/**
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 허용할 오리진 설정
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:8081"
        ));

        // 허용할 HTTP 메서드 설정
        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        // 허용할 헤더 설정 (모든 헤더 허용)
        config.setAllowedHeaders(List.of("*"));

        // 응답 시 노출할 헤더 설정
        config.setExposedHeaders(List.of("Authorization"));

        // 쿠키 및 인증 정보 허용
        config.setAllowCredentials(true);
        config.setMaxAge(3600L); //프리플라이트 캐시

        // CORS 설정을 URL 패턴에 등록
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return source;
    }
}

