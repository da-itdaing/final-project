# 테스트 수정 내역

> 작성일: 2025-11-11  
> 목적: 전체 테스트 스위트 통과를 위한 수정 사항 기록

## 📊 수정 개요

- **수정 전 상태**: 15개 테스트 실패
- **수정 후 상태**: ✅ 전체 115개 테스트 통과 (13개 스킵)
- **수정 파일 수**: 10개

---

## 🔧 주요 수정 사항

### 1. 판매자 회원가입 필수 필드 변경

**파일**: `src/main/java/com/da/itdaing/domain/user/dto/AuthDto.java`

#### 변경 내용
```java
// 변경 전
@Schema(description = "활동 지역 (선택)", example = "광주/남구")
@Size(max = 255, message = "활동 지역은 255자 이하여야 합니다")
private String activityRegion;

// 변경 후
@Schema(description = "활동 지역", example = "광주/남구", requiredMode = Schema.RequiredMode.REQUIRED)
@NotBlank(message = "활동 지역은 필수입니다")
@Size(max = 255, message = "활동 지역은 255자 이하여야 합니다")
private String activityRegion;
```

#### 이유
- `sellerSignup_validation_400_whenMissingActivityRegion` 테스트가 `activityRegion` 누락 시 400 에러를 기대
- 모든 성공 케이스 테스트에서 `activityRegion`을 포함하고 있어 필수 필드로 판단

#### 영향받은 테스트
- ✅ `AuthControllerSignupTest.sellerSignup_validation_400_whenMissingActivityRegion()`
- ✅ `AuthControllerTest.signupSeller_Success()` - activityRegion 추가 필요

---

### 2. 회원가입 엔드포인트 상태 코드 변경

**파일**: 
- `src/main/java/com/da/itdaing/domain/user/api/AuthController.java`
- `src/test/java/com/da/itdaing/domain/user/api/AuthControllerTest.java`
- `src/test/java/com/da/itdaing/domain/user/api/AuthControllerSignupTest.java`

#### 변경 내용
```java
// 변경 전
@PostMapping("/auth/signup/consumer")
public ApiResponse<SignupResponse> signupConsumer(@Valid @RequestBody SignupConsumerRequest request) {
    return ApiResponse.success(authService.signupConsumer(request));
}

// 변경 후
@PostMapping("/auth/signup/consumer")
public ResponseEntity<ApiResponse<SignupResponse>> signupConsumer(@Valid @RequestBody SignupConsumerRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(authService.signupConsumer(request)));
}
```

#### 이유
- RESTful API 관례: 리소스 생성 성공 시 **201 Created** 상태 코드 반환
- 기본 Spring MVC는 `ApiResponse<T>` 반환 시 200 OK로 응답
- 명시적 상태 코드 제어를 위해 `ResponseEntity` 사용 필요

#### 영향받은 테스트
- ✅ `AuthControllerTest.signupConsumer_Success()` - `.andExpect(status().isCreated())`로 변경
- ✅ `AuthControllerTest.signupSeller_Success()` - `.andExpect(status().isCreated())`로 변경
- ✅ `AuthControllerSignupTest.consumerSignup_success_201()`
- ✅ `AuthControllerSignupTest.sellerSignup_success_201()`

---

### 3. SellerProfileController JSON 직렬화 보장

**파일**: 
- `src/main/java/com/da/itdaing/domain/seller/api/SellerProfileController.java`
- `src/test/java/com/da/itdaing/domain/seller/api/SellerProfileControllerTest.java`

#### 변경 내용
```java
// 컨트롤러 수정
@GetMapping("/api/sellers/me/profile")
public ResponseEntity<ApiResponse<SellerProfileResponse>> getMyProfile(@AuthenticationPrincipal UserPrincipal principal) {
    SellerProfileResponse resp = sellerProfileService.getMyProfile(principal.getUserId());
    return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(ApiResponse.success(resp));
}

// 테스트 수정 - standaloneSetup 사용
@BeforeEach
void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(sellerProfileController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
}
```

#### 이유
- `@WebMvcTest`에서 JSON 응답 body가 비어있는 문제 발생
- Spring Boot 3.5.6에서 `@WebMvcTest`가 handler mapping을 올바르게 구성하지 못하는 이슈
- `standaloneSetup`으로 명시적 MockMvc 구성 필요

#### 영향받은 테스트
- ✅ `SellerProfileControllerTest.getMyProfile_success()`
- ✅ `SellerProfileControllerTest.updateMyProfile_success()`

---

### 4. RefreshToken 삭제 메서드 반환 타입 수정

**파일**: 
- `src/main/java/com/da/itdaing/domain/user/repository/RefreshTokenRepository.java`
- `src/test/java/com/da/itdaing/domain/user/repository/RefreshTokenRepositoryTest.java`

#### 변경 내용
```java
// 변경 전
@Modifying
@Query("delete from RefreshToken rt where rt.user.id = :userId")
long deleteAllByUserId(@Param("userId") Long userId);

// 변경 후
@Modifying
@Query("delete from RefreshToken rt where rt.user.id = :userId")
int deleteAllByUserId(@Param("userId") Long userId);
```

#### 이유
- Spring Data JPA 규약: `@Modifying` 쿼리는 **void 또는 int/Integer**만 반환 가능
- `long` 반환 타입 사용 시 `IllegalArgumentException` 발생

#### 에러 메시지
```
java.lang.IllegalArgumentException: Modifying queries can only use void or int/Integer as return type
```

#### 영향받은 테스트
- ✅ `RefreshTokenRepositoryTest.deleteAllByUserId_success()`

---

### 5. JPA Auditing 활성화

**파일**: `src/test/java/com/da/itdaing/domain/user/repository/RefreshTokenRepositoryTest.java`

#### 변경 내용
```java
// 변경 전
@DataJpaTest
@ActiveProfiles("test")
class RefreshTokenRepositoryTest { ... }

// 변경 후
@JpaSliceTest  // @Import(JpaConfig.class) 포함
class RefreshTokenRepositoryTest { ... }
```

#### 이유
- `Users` 엔티티가 `BaseTimeEntity`를 상속하며 `@CreatedDate`, `@LastModifiedDate` 사용
- `@DataJpaTest`는 JPA Auditing을 자동 활성화하지 않음
- `@JpaSliceTest`는 `JpaConfig`를 import하여 `@EnableJpaAuditing` 적용

#### 에러 메시지
```
org.hibernate.exception.ConstraintViolationException: NULL not allowed for column "CREATED_AT"
```

#### 영향받은 테스트
- ✅ 모든 RefreshTokenRepositoryTest (6개 테스트)

---

### 6. Spring Security 필터 비활성화

**파일**: `src/test/java/com/da/itdaing/global/error/GlobalExceptionHandlerTest.java`

#### 변경 내용
```java
// 변경 전
@WebMvcTest(TestController.class)
class GlobalExceptionHandlerTest { ... }

// 변경 후
@WebMvcTest(TestController.class)
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest { ... }
```

#### 이유
- `@WebMvcTest`는 Spring Security를 자동으로 활성화
- CSRF 토큰 없이 POST 요청 시 **403 Forbidden** 응답
- 컨트롤러 슬라이스 테스트에서는 인증/인가 로직 제외 필요

#### 영향받은 테스트
- ✅ `GlobalExceptionHandlerTest.whenValidationFails_thenReturnsApiErrorResponse()`
- ✅ `GlobalExceptionHandlerTest.whenValidationSucceeds_thenReturnsSuccessResponse()`
- ✅ `GlobalExceptionHandlerTest.whenNameLengthInvalid_thenReturnsFieldError()`
- ✅ `GlobalExceptionHandlerTest.whenEmailFormatInvalid_thenReturnsFieldError()`

---

### 7. Bean Validation 에러 개수 조정

**파일**: `src/test/java/com/da/itdaing/global/error/GlobalExceptionHandlerTest.java`

#### 변경 내용
```java
// 변경 전
.andExpect(jsonPath("$.error.fieldErrors.length()").value(2));

// 변경 후
.andExpect(jsonPath("$.error.fieldErrors.length()").value(3));
```

#### 이유
- 빈 `name` 값은 **두 개의 검증 제약**을 위반:
  1. `@NotBlank(message = "이름은 필수입니다")`
  2. `@Size(min = 2, max = 10, message = "이름은 2자 이상 10자 이하여야 합니다")`
- 무효한 `email` 값은 **한 개의 검증 제약** 위반:
  1. `@Email(message = "올바른 이메일 형식이 아닙니다")`
- 총 3개의 field error 발생

#### 실제 응답
```json
{
  "success": false,
  "error": {
    "status": 400,
    "code": "E001",
    "message": "입력값이 올바르지 않습니다",
    "fieldErrors": [
      {"field": "name", "value": "", "reason": "이름은 필수입니다"},
      {"field": "name", "value": "", "reason": "이름은 2자 이상 10자 이하여야 합니다"},
      {"field": "email", "value": "invalid-email", "reason": "올바른 이메일 형식이 아닙니다"}
    ]
  }
}
```

---

## 📝 테스트 수정 패턴 요약

### 패턴 1: Bean Validation 필수 필드 추가
```java
// 모든 회원가입 테스트에 필수 필드 추가
AuthDto.SignupConsumerRequest request = AuthDto.SignupConsumerRequest.builder()
    .featureIds(List.of(1L))  // ✅ 추가
    .interestCategoryIds(List.of(1L))
    .styleIds(List.of(1L))
    .regionIds(List.of(1L))
    .build();
```

### 패턴 2: HTTP 상태 코드 검증 변경
```java
// 리소스 생성 테스트
.andExpect(status().isCreated())  // 200 → 201
```

### 패턴 3: @WebMvcTest에서 Security 제외
```java
@WebMvcTest(Controller.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(exclude = {
    SecurityAutoConfiguration.class,
    SecurityFilterAutoConfiguration.class
})
```

### 패턴 4: Repository 테스트에서 Auditing 활성화
```java
@JpaSliceTest  // @DataJpaTest + @Import(JpaConfig.class)
class RepositoryTest { ... }
```

---

## ✅ 최종 결과

### 테스트 실행 명령어
```bash
./gradlew test --quiet
```

### 실행 결과
```
115 tests completed, 0 failed, 13 skipped
BUILD SUCCESSFUL
```

### 스킵된 테스트 (13개)
- `MasterQueryControllerIntegrationTest` (통합 테스트, 필요시 실행)
- `MasterQueryControllerTest` 일부 (기능 개발 중)

---

## 🎯 교훈 및 Best Practices

### 1. ResponseEntity 명시적 사용
- Spring MVC는 DTO 직접 반환 시 200 OK 고정
- 상태 코드 제어가 필요하면 `ResponseEntity` 사용

### 2. Bean Validation 제약 조건 이해
- 하나의 필드에 여러 제약 조건 적용 가능
- 테스트 시 모든 위반 사항이 field errors에 포함됨

### 3. @WebMvcTest 사용 시 주의사항
- Spring Security 자동 활성화
- JSON 직렬화 문제 발생 시 `standaloneSetup` 고려
- 필요한 경우 `@AutoConfigureMockMvc(addFilters = false)` 사용

### 4. Spring Data JPA 규약 준수
- `@Modifying` 쿼리는 void/int/Integer만 반환
- `@Query`에서 primitive long 사용 불가

### 5. JPA Auditing 설정
- `@DataJpaTest`는 Auditing 미포함
- 커스텀 `@JpaSliceTest` 어노테이션으로 해결

---

## 📚 참고 문서

- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring MVC Test Framework](https://docs.spring.io/spring-framework/reference/testing/spring-mvc-test-framework.html)
- [Bean Validation Specification](https://beanvalidation.org/2.0/spec/)
- [HTTP Status Codes - RFC 9110](https://www.rfc-editor.org/rfc/rfc9110.html#name-status-codes)
