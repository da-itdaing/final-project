package com.da.itdaing.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.ExternalDocumentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

/**
 * OpenAPI(Swagger) 설정
 */
@Configuration
public class OpenApiConfig {

    @Value("${app.version:v2.0.0}")
    private String appVersion;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DA-ITDAING API")
                        .description("""
                                DA-ITDAING 팝업스토어 플랫폼 API 문서

                                ## 인증 방식
                                JWT Bearer Token 기반 인증을 사용합니다.

                                1. `/api/auth/login` 엔드포인트로 로그인하여 JWT 토큰을 발급받습니다.
                                2. 발급받은 토큰을 Authorization 헤더에 포함하여 API를 호출합니다.
                                   - 형식: `Authorization: Bearer {token}`

                                ## 사용자 역할
                                - **CONSUMER**: 일반 소비자 (팝업스토어 검색, 위시리스트, 리뷰 작성)
                                - **SELLER**: 판매자 (팝업스토어 등록 및 관리)
                                - **ADMIN**: 관리자 (시스템 관리 및 승인)

                                ## JWT 토큰 구조
                                JWT 토큰에는 다음 클레임이 포함됩니다:
                                - `sub`: 사용자 ID
                                - `role`: ROLE_CONSUMER, ROLE_SELLER, ROLE_ADMIN 중 하나
                                - `iss`: itdaing-server
                                - `iat`: 발급 시각 (Unix timestamp)
                                - `exp`: 만료 시각 (발급 후 24시간)

                                ## 공개 API
                                다음 엔드포인트는 인증 없이 접근 가능합니다:
                                - `/api/auth/**`: 인증 관련 API (회원가입, 로그인, 토큰 재발급, 로그아웃)
                                - `GET /api/master/**`: 마스터 데이터 조회 (지역, 스타일, 카테고리, 특징 등)

                                ## 기본 URL
                                - 로컬: http://localhost:8080
                                - 운영: https://aischool.daitdaing.link
                                """)
                        .version(appVersion)
                )
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local"),
                        new Server().url("https://aischool.daitdaing.link").description("Production")
                ))
                .tags(List.of(
                        new Tag().name("Auth").description("인증/인가 및 사용자 세션 관리 API"),
                        new Tag().name("Master").description("카테고리/스타일/지역/특징 등 마스터 데이터 조회 API"),
                        new Tag().name("Seller Profile").description("판매자 프로필 조회/등록/수정 API")
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Docs & Swagger UI")
                        .url("https://da-itdaing.github.io/final-project/"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("""
                                        JWT 액세스 토큰을 입력하세요.

                                        토큰 발급:
                                        1. POST /api/auth/login으로 로그인
                                        2. 응답의 accessToken 값을 복사
                                        3. 우측 상단 'Authorize' 버튼 클릭 후 토큰 입력

                                        예시: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfQ09OU1VNRVIifQ...
                                        """)
                        )
                )
                // 전역 보안 요구사항 적용 (공개 API는 각 엔드포인트에서 @Operation(security = {})로 제외)
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}

