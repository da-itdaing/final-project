package com.da.itdaing.domain.user.api;

import com.da.itdaing.domain.user.dto.AuthDto;
import com.da.itdaing.domain.user.service.AuthService;
import com.da.itdaing.global.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 API 컨트롤러
 */
@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "소비자 회원가입",
            description = """
                    일반 소비자 계정을 생성합니다.

                    가입된 사용자는 CONSUMER 역할이 부여되며, 다음 권한을 가집니다:
                    - 팝업스토어 검색 및 조회
                    - 위시리스트 관리
                    - 리뷰 작성 및 조회
                    - 소비자 선호 정보 설정

                    로그인 시 JWT 토큰의 role 클레임에는 "ROLE_CONSUMER"가 설정됩니다.
                    """,
            security = {}
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "success": true,
                                        "data": {
                                            "userId": 1,
                                            "email": "consumer1@example.com",
                                            "role": "CONSUMER"
                                        }
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "입력값 검증 실패",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "success": false,
                                        "error": {
                                            "status": 400,
                                            "code": "COMMON-001",
                                            "message": "입력값이 올바르지 않습니다"
                                        }
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "이메일 중복",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "success": false,
                                        "error": {
                                            "status": 409,
                                            "code": "COMMON-409",
                                            "message": "이미 사용 중인 이메일입니다"
                                        }
                                    }
                                    """)
                    )
            )
    })
    @PostMapping("/auth/signup/consumer")
    public ResponseEntity<ApiResponse<AuthDto.SignupResponse>> signupConsumer(
        @Valid @RequestBody AuthDto.SignupConsumerRequest request) {
        var response = authService.signupConsumer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @Operation(
            summary = "판매자 회원가입",
            description = """
                    팝업스토어 판매자 계정을 생성합니다.

                    가입된 사용자는 SELLER 역할이 부여되며, 다음 권한을 가집니다:
                    - 팝업스토어 등록 및 관리
                    - 판매자 프로필 관리
                    - 팝업스토어 승인 요청
                    - 고객 리뷰 조회

                    로그인 시 JWT 토큰의 role 클레임에는 "ROLE_SELLER"가 설정됩니다.
                    """,
            security = {}
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "회원가입 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "success": true,
                                        "data": {
                                            "userId": 2,
                                            "email": "seller@example.com",
                                            "role": "SELLER"
                                        }
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "입력값 검증 실패",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "success": false,
                                        "error": {
                                            "status": 400,
                                            "code": "COMMON-001",
                                            "message": "입력값이 올바르지 않습니다"
                                        }
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "이메일 중복",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "success": false,
                                        "error": {
                                            "status": 409,
                                            "code": "COMMON-409",
                                            "message": "이미 사용 중인 이메일입니다"
                                        }
                                    }
                                    """)
                    )
            )
    })
    @PostMapping("/auth/signup/seller")
    public ResponseEntity<ApiResponse<AuthDto.SignupResponse>> signupSeller(
        @Valid @RequestBody AuthDto.SignupSellerRequest request) {
        var response = authService.signupSeller(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @Operation(
            summary = "로그인",
            description = """
                    이메일과 비밀번호로 로그인하여 JWT 액세스 토큰을 발급받습니다.

                    JWT 토큰에는 다음 클레임이 포함됩니다:
                    - sub: 사용자 ID
                    - role: ROLE_CONSUMER, ROLE_SELLER, ROLE_ADMIN 중 하나
                    - iss: itdaing-server
                    - iat: 발급 시각 (Unix timestamp)
                    - exp: 만료 시각 (발급 후 24시간)

                    발급받은 토큰은 Authorization 헤더에 "Bearer {token}" 형식으로 포함하여 인증이 필요한 API를 호출할 수 있습니다.

                    예시 토큰 페이로드 (디코딩 후):
                    {
                      "sub": "1",
                      "role": "ROLE_CONSUMER",
                      "iss": "itdaing-server",
                      "iat": 1730419200,
                      "exp": 1730505600
                    }
                    """,
            security = {}
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "로그인 성공 응답",
                                    description = "소비자(CONSUMER) 계정으로 로그인한 경우",
                                    value = """
                                    {
                                        "success": true,
                                        "data": {
                                            "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfQ09OU1VNRVIiLCJpc3MiOiJpdGRhaW5nLXNlcnZlciIsImlhdCI6MTczMDQxOTIwMCwiZXhwIjoxNzMwNTA1NjAwfQ.signature"
                                        }
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "success": false,
                                        "error": {
                                            "status": 401,
                                            "code": "AUTH-401",
                                            "message": "이메일 또는 비밀번호가 올바르지 않습니다"
                                        }
                                    }
                                    """)
                    )
            )
    })
    @PostMapping("/auth/login")
    public ApiResponse<AuthDto.LoginResponse> login(
            @Valid @RequestBody AuthDto.LoginRequest request
    ) {
        AuthDto.LoginResponse response = authService.login(request);
        return ApiResponse.success(response);
    }

    @Operation(
            summary = "내 프로필 조회",
            description = """
                    인증된 사용자의 프로필 정보를 조회합니다.

                    이 API는 JWT 토큰 인증이 필요합니다.
                    Authorization 헤더에 "Bearer {token}" 형식으로 토큰을 포함해야 합니다.

                    응답의 role 필드는 다음 값 중 하나입니다:
                    - CONSUMER: 일반 소비자
                    - SELLER: 팝업스토어 판매자
                    - ADMIN: 시스템 관리자
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "소비자 프로필",
                                            description = "CONSUMER 역할의 사용자 프로필",
                                            value = """
                                            {
                                                "success": true,
                                                "data": {
                                                    "id": 1,
                                                    "email": "consumer@example.com",
                                                    "name": "김소비",
                                                    "nickname": "소비왕",
                                                    "role": "CONSUMER"
                                                }
                                            }
                                            """),
                                    @ExampleObject(
                                            name = "판매자 프로필",
                                            description = "SELLER 역할의 사용자 프로필",
                                            value = """
                                            {
                                                "success": true,
                                                "data": {
                                                    "id": 2,
                                                    "email": "seller@example.com",
                                                    "name": "박판매",
                                                    "nickname": "팝업왕",
                                                    "role": "SELLER"
                                                }
                                            }
                                            """)
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 - 토큰이 없거나 유효하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "success": false,
                                        "error": {
                                            "status": 401,
                                            "code": "AUTH-401",
                                            "message": "인증이 필요합니다"
                                        }
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "success": false,
                                        "error": {
                                            "status": 404,
                                            "code": "COMMON-404",
                                            "message": "사용자를 찾을 수 없습니다"
                                        }
                                    }
                                    """)
                    )
            )
    })
    @GetMapping("/users/me")
    public ApiResponse<AuthDto.UserProfileResponse> getMyProfile(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        AuthDto.UserProfileResponse response = authService.getProfile(userId);
        return ApiResponse.success(response);
    }
}

