# itdaing-server

> Spring Boot 3.5 / Java 21 기반 백엔드 서비스. Pop-up 스토어 검색 · 추천 · 운영을 위한 도메인 집합을 제공합니다.

**Last Updated:** 2025-11-09

## Table of Contents
1. [개요 & 기술 스택](#개요--기술-스택)
2. [프로파일 개요](#프로파일-개요)
3. [빠른 시작](#빠른-시작-로컬)
4. [JWT 설정](#jwt-설정-메모)
5. [테스트 실행](#테스트)
6. [OpenAPI / Swagger 배포](#openapiswagger를-github-pages로-공개하기)
7. [EC2 배포](#ec2-배포-docker-없이)
8. [IDE 가이드](#ide-실행-가이드)
9. [도메인 구조 요약](#도메인-구조-요약)
10. [품질 & 규약](#품질--규약)
11. [문서 네비게이션](#문서-네비게이션)
12. [라이선스](#라이선스)

## 개요 & 기술 스택

- 빌드 도구: Gradle Kotlin DSL
- JDK: 21 (Temurin 권장)
- 주요 라이브러리:
	- Spring Boot: Web, Security, Data JPA, Validation, Actuator
	- Infra: Flyway, H2/MySQL, AWS SDK (S3) 예정
	- API 문서: springdoc-openapi + Swagger UI
	- Auth: jjwt (HS256), Spring Security
	- Query: QueryDSL (Jakarta), MapStruct (DTO 매핑)
	- Test: JUnit 5, AssertJ

아키텍처는 **도메인 모듈화 패키지** (domain.user, domain.master, domain.popup …) 패턴을 따르며 서비스 / 리포지토리 / DTO / 컨트롤러를 계층적으로 구성합니다.

## 프로파일 개요

- `local` (기본): H2 메모리 DB, Swagger UI 활성화, 빠른 개발용
- `dev`: IDE에서 RDS/S3 등 외부 리소스와 연동하는 개발용 (환경변수 주입)
- `prod`: EC2 배포용 (포트 80, 환경변수 기반). 운영 키/비밀번호는 절대 커밋하지 않음

프로파일 활성화 방법:

```bash
# 예) local
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun

# 예) dev (RDS/S3 사용)
SPRING_PROFILES_ACTIVE=dev DB_URL=jdbc:mysql://<rds-endpoint>:3306/<db> \
DB_USERNAME=<user> DB_PASSWORD=<pass> S3_BUCKET_NAME=<bucket> \
./gradlew bootRun
```

## 필수 요구사항

- Java 21 설치 (macOS는 Homebrew의 openjdk@21 권장)
- IDE에서 Gradle 프로젝트로 Import, Gradle JVM은 JDK 21 지정

## 빠른 시작 (로컬)

```bash
./gradlew bootRun
# 혹은 명시적으로
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- 헬스 체크: http://localhost:8080/actuator/health
	- liveness: `/actuator/health` / readiness: 향후 커스터마이즈 가능

루트 "/"는 인증 필요로 401이 정상입니다. 공개 엔드포인트는 `/api/master/**`(GET), `/api/auth/**`, `/v3/api-docs/**`, `/swagger-ui/**`, `/actuator/health` 입니다.

## 도메인 구조 요약

| 도메인 | 주요 엔티티 | 주요 기능 |
|--------|-------------|-----------|
| master | Region / Style / Category / Feature | 마스터 데이터 조회 |
| user   | Users / ConsumerProfile / SellerProfile / Preferences | 가입, 로그인, 프로필, 선호도 |
| popup  | Popup / PopupImage / PopupCategory / PopupFeature | 팝업 스토어 등록/관리 |
| social | Wishlist / Review / ReviewImage | 사용자 상호작용 |
| reco   | Daily*Recommendation / UserRecoDismissal | 추천 결과 저장 |
| metric | EventLog / DailyMetric* | 지표 수집 |
| audit  | ApprovalRecord | 승인 / 변경 기록 |
| messaging | Message / Attachment / Announcement | 알림/메시지 전송 |

세부 테스트 태스크는 `docs/DOMAIN_TESTS.md` 참고.

## JWT 설정 메모

- HS256은 최소 256비트(32바이트) 이상의 secret을 요구합니다.
- `application-local.yml`, `application-dev.yml`, `application-openapi.yml`의 기본값은 충분한 길이로 설정되어 있습니다.
- 만료시간은 밀리초(long)로 주입합니다.

환경변수로 덮어쓰기 예:

```bash
export JWT_SECRET='your-very-long-32+bytes-secret................................'
export JWT_ACCESS_TOKEN_EXPIRATION=900000
export JWT_REFRESH_TOKEN_EXPIRATION=1209600000
```

## 테스트

```bash
./gradlew test

# 특정 패키지/클래스만
./gradlew test --tests '*RepositoryTest'
```

일부 컨트롤러 테스트(판매자 프로필) 실패 케이스가 있으며, 실행에는 영향을 주지 않습니다. 필요 시 별도 이슈로 보정 가능합니다.

## 품질 & 규약

- 코드 스타일: 기본 IntelliJ + `-parameters` 컴파일 옵션 사용 (리플렉션 파라미터 이름 유지)
- Null 처리: Controller → Service 간 DTO/Optional 명확화. Optional은 Service 레이어 내부에서만 사용하도록 제한 예정.
- 에러 응답: 표준 코드 (COMMON-*, AUTH-*) 사용. 상세 표는 `docs/JWT_AUTH_IMPLEMENTATION_SUMMARY.md` / `docs/AUTH_SWAGGER_UPDATE_SUMMARY.md` 참고.
- 마이그레이션: Flyway baseline 후 변경 스크립트 (`db/migration/V***__desc.sql`).
- 보안: 운영 secret/JWT key는 절대 커밋 금지. 로컬/테스트는 안전한 placeholder.

## 문서 네비게이션

| 문서 | 목적 |
|------|------|
| `docs/IDE_SETUP.md` | IDE 및 원격 디버깅 설정 |
| `docs/DEPLOY_EC2.md` | Docker 없이 EC2 배포 절차 |
| `docs/DOMAIN_TESTS.md` | 도메인별 테스트 태스크 및 규칙 |
| `docs/JWT_AUTH_IMPLEMENTATION_SUMMARY.md` | JWT & 보안 구현 요약 |
| `docs/AUTH_SWAGGER_UPDATE_SUMMARY.md` | Swagger 문서 업데이트 내역 |
| `docs/AUTHCONTROLLER_RECOVERY_REPORT.md` | AuthController 복구 작업 보고 |
| `docs/CONTROLLER_AUDIT_EMPTY_BODY_REPORT.md` | 컨트롤러 응답 본문 감사 결과 |

## EC2 배포 (Docker 없이)

- 문서: `docs/DEPLOY_EC2.md` 참조
- 핵심: `application-prod.yml` + 환경변수 기반 구성, systemd로 서비스 관리

## IDE 실행 가이드

- 문서: `docs/IDE_SETUP.md` 참조 (IntelliJ / Eclipse 세팅, ProxyJump, 원격 디버그)

## OpenAPI/Swagger를 GitHub Pages로 공개하기

본 레포지토리는 OpenAPI 문서를 Gradle 태스크로 생성하고, GitHub Pages(gh-pages 브랜치)에 정적 Swagger UI를 배포하는 워크플로를 포함합니다.

### 1) 문서 생성 (로컬)

```bash
./gradlew generateOpenApiDocs
# 산출물: build/openapi/openapi.yaml
```

### 2) GitHub Pages 퍼블리시 (CI)

- 워크플로: `.github/workflows/publish-openapi.yml`
- 트리거: 기본 push 및 수동 실행(workflow_dispatch)
- 첫 실행 후 GitHub Pages 설정에서 Source를 `gh-pages` 브랜치로 지정하세요.

배포 주소(예시):

- 사용자/오거나이제이션 페이지: https://da-itdaing.github.io/final-project/

조직 정책상 `GITHUB_TOKEN`에 write 권한을 줄 수 없을 때는 ‘배포 키(Deploy Key)’로 배포합니다.

### 권한 정책이 엄격한 조직에서의 설정(Deploy Key 사용)

1) 로컬에서 배포 전용 키 생성(비밀번호 없이):
```bash
ssh-keygen -t ed25519 -C "gh-pages deploy" -f gh-pages -N ""
```
2) GitHub → Repository → Settings → Deploy keys → Add deploy key
	- Title: gh-pages
	- Key: `gh-pages.pub` 내용 붙여넣기
	- Allow write access 체크
3) GitHub → Repository → Settings → Secrets and variables → Actions → New repository secret
	- Name: `GH_PAGES_DEPLOY_KEY`
	- Secret: `gh-pages`(개인키) 파일 내용 전체 붙여넣기
4) Actions 탭에서 "Publish OpenAPI to GitHub Pages" 실행
5) Settings → Pages → Branch: `gh-pages` / Folder: `/ (root)` 설정

위 절차를 마치면 상단 주소에서 Swagger UI가 공개됩니다.

## 라이선스

사내/프로젝트 정책에 따릅니다. 필요 시 OSS 라이선스 명시 블록 추가 예정.

---
문의 / 개선 제안: Issue 등록 또는 담당자에게 직접 전달.
