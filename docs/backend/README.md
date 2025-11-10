# Backend 문서

백엔드 개발자를 위한 문서 모음입니다.

## 📚 문서 목록

### 테스트 관련
- **[TEST_FIXES.md](./TEST_FIXES.md)** - 테스트 수정 내역 및 패턴
  - 115개 테스트 통과를 위한 수정 사항
  - Bean Validation, HTTP 상태 코드, JPA Auditing 등
  - Best Practices 및 교훈

- **[DOMAIN_TESTS.md](./DOMAIN_TESTS.md)** - 도메인별 테스트 가이드
  - Repository, Service, Controller 테스트 작성법
  - 테스트 데이터 준비 및 검증 방법

### 인증/인가
- **[JWT_AUTH_IMPLEMENTATION_SUMMARY.md](./JWT_AUTH_IMPLEMENTATION_SUMMARY.md)** - JWT 인증 구현 요약
  - Access Token / Refresh Token 구조
  - 토큰 갱신 로직
  - Security Filter Chain 설정

- **[AUTHCONTROLLER_RECOVERY_REPORT.md](./AUTHCONTROLLER_RECOVERY_REPORT.md)** - AuthController 복구 보고서
  - 인증 컨트롤러 이슈 및 해결 과정

### API 문서화
- **[AUTH_SWAGGER_UPDATE_SUMMARY.md](./AUTH_SWAGGER_UPDATE_SUMMARY.md)** - Swagger 문서 업데이트 요약
  - OpenAPI 스펙 작성 방법
  - Springdoc 설정

- **[CONTROLLER_AUDIT_EMPTY_BODY_REPORT.md](./CONTROLLER_AUDIT_EMPTY_BODY_REPORT.md)** - 컨트롤러 빈 응답 이슈 보고서
  - JSON 직렬화 문제 해결

## 🔗 관련 리소스

### API 스펙
- OpenAPI 스펙: `docs/openapi.json`
- 데이터베이스 스키마: `docs/schema/dbdiagram_v2.dbml`

### 개발 환경
- IDE 설정: [`docs/team/IDE_SETUP.md`](../team/IDE_SETUP.md)
- 브랜치 전략: [`docs/team/BRANCHING.md`](../team/BRANCHING.md)

## 🛠️ 빠른 시작

### 프로젝트 실행
```bash
# 테스트 실행
./gradlew test

# 애플리케이션 실행
./gradlew bootRun

# 코드 포맷 체크
./gradlew spotlessCheck

# 코드 포맷 적용
./gradlew spotlessApply
```

### API 문서 확인
```bash
# 애플리케이션 실행 후
open http://localhost:8080/swagger-ui.html
```

## 📝 코드 작성 가이드

### 필수 사항
- ✅ 모든 API는 `ApiResponse<T>` 래퍼 사용
- ✅ 상태 코드 명시적 제어는 `ResponseEntity` 사용
- ✅ Bean Validation으로 DTO 검증
- ✅ 테스트 커버리지 유지 (단위 테스트 + 통합 테스트)

### 권장 사항
- 📌 Gitmoji로 커밋 메시지 작성
- 📌 PR 템플릿 활용
- 📌 코드 리뷰 필수

## 🔍 문제 해결

### 테스트 실패 시
1. `TEST_FIXES.md` 참고하여 유사 패턴 확인
2. Bean Validation 제약 조건 확인
3. HTTP 상태 코드 기대값 확인
4. JPA Auditing 활성화 확인 (`@JpaSliceTest` 사용)

### API 계약 변경 시
1. OpenAPI 스펙 업데이트
2. Contract Check CI 통과 확인
3. 프론트엔드 팀에 breaking change 공유
