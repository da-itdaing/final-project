# final-project

이 레포지토리는 최상단에 `backend/`(Spring Boot)와 `frontend/`(React + TypeScript)만 두고, Docker 기반 로컬 개발을 우선으로 구성되어 있습니다. 실제 배포(예: AWS ECS)는 추후 적용을 염두에 두고 설계했습니다. API 규격은 백엔드를 기준으로 하며, 기본 경로는 `/api`입니다.

## 바로 실행 (로컬, Docker Compose)

```bash
# 레포지토리 루트에서 실행
docker compose up --build -d
# Frontend: http://localhost:8081
# Backend:  http://localhost:8080
```

종료:

```bash
docker compose down
```

## 로컬 동작 방식 개요

- API 기본 경로: `/api`
- 프론트엔드 컨테이너(Nginx)가 정적 파일(SPA)을 80 포트로 서비스하고, `/api/*` 요청을 Docker 네트워크의 `backend:8080`으로 프록시합니다.
- 백엔드는 `local` 프로파일에서 `server.servlet.context-path=/api`로 동작합니다.
- 브라우저는 프론트엔드로만 요청을 보내므로 CORS 이슈 없이 개발할 수 있습니다.

## 폴더 구조

- `backend/`
  - `Dockerfile` — Gradle 빌드 → Temurin JRE 런타임 멀티스테이지 이미지
  - `docker-compose.yml` — 백엔드 단독 실행용(선택)
  - `src/main/resources/application.yml` — 기본 설정 (환경변수 기반: DB_*, JWT_*)
  - `src/main/resources/application-local.yml` — 로컬 전용 설정(H2, `/api` 컨텍스트 경로)
  - `.env.example` / `.env` — 로컬용 환경 변수 샘플/실제값(민감정보는 샘플로만 커밋)
- `frontend/`
  - `Dockerfile` — Node 빌드 → Nginx 서빙 멀티스테이지 이미지
  - `docker-compose.yml` — 프론트 단독 실행용(선택)
  - `nginx.conf` — SPA 라우팅 + `/api/` → 백엔드 리버스 프록시

## 개별 서비스 실행

백엔드만 실행:
```bash
cd backend
docker compose up --build -d
# http://localhost:8080
```

프론트엔드만 실행:
```bash
cd frontend
docker compose up --build -d
# http://localhost:8081
```

## 프론트엔드 설정

프론트 빌드는 Vite 환경 변수를 빌드 시 주입합니다:
- `VITE_USE_REAL_API=true` — 목 데이터가 아닌 실제 백엔드 사용
- `VITE_API_BASE_URL` — Docker 빌드에서는 비워둠(코드에서 `/api/...` 절대 경로 사용)

빠른 UI 개발(HMR) 옵션:
```bash
cd frontend
npm install
npm run dev
# 필요 시 Vite dev server proxy로 /api를 백엔드로 프록시 설정 가능
```

## 백엔드 설정

`local` 프로파일은 H2를 사용하고 컨텍스트 경로를 `/api`로 설정합니다. 기본 설정(`application.yml`)은 다음 환경 변수를 참조합니다:
- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASS`
- `JWT_SECRET`, `ACCESS_EXP_MS`

Compose에서 활성 프로파일을 바꾸고 싶다면:
```yaml
# docker-compose.yml
services:
  backend:
    environment:
      - SPRING_PROFILES_ACTIVE=local
```

민감정보 관리:
- `.env.example`는 샘플만 커밋하고, 실제 `.env`는 로컬에서만 유지하세요.
- 운영/스테이징은 시크릿 매니저 또는 ECS 태스크 정의 환경 변수 사용을 권장합니다.

## 배포(미리보기: AWS ECS 고려)

- API 경로 `/api`를 일관되게 유지하면 프론트 코드 변경 없이 라우팅만으로 배포 구성이 가능합니다.
- 두 가지 대표 패턴:
  1) Nginx 컨테이너로 SPA 서빙(서비스 A) + 백엔드 서비스(서비스 B)를 ALB 뒤에 두고, Nginx에서 `/api`를 B로 리버스 프록시
  2) SPA는 S3/CloudFront에 정적 호스팅, ALB에서 `/api/*`를 백엔드로 라우팅(프론트는 계속 `/api/...` 호출)

## 개발 워크플로 팁

- 백엔드 변경이 잦다면: 컨테이너 재빌드가 부담될 수 있습니다. IDE에서 로컬 실행(Gradle) + 프론트는 Vite dev proxy로 연동하는 방식도 고려하세요.
- 프론트 HMR: `npm run dev`로 화면 피드백 속도를 높일 수 있습니다. `/api` 프록시는 `nginx.conf` 또는 Vite dev server 설정 중 하나만 사용하면 됩니다.

## 트러블슈팅

- 포트 충돌: 8080/8081을 사용하는 다른 프로세스가 있는지 확인하세요.
- 백엔드 기동 지연: 첫 빌드는 Gradle 캐시로 시간이 걸릴 수 있습니다. 기동 후 헬스체크 대기(수십 초) 필요할 수 있습니다.
- CORS 오류: 로컬 구성에서는 Nginx 프록시 때문에 발생하지 않아야 합니다. 직접 백엔드에 브라우저에서 호출하면 CORS가 발생할 수 있습니다.
- Apple Silicon: `node:20-alpine`, `eclipse-temurin:21-jre` 이미지는 ARM64를 지원합니다.

## 유지보수 가이드

- 레포 최상단은 `backend/`, `frontend/`만 유지합니다(기타 산출물은 각 폴더 내부 또는 빌드 산출물로 제한).
- 환경 변수/시크릿은 코드에 하드코딩하지 말고 `.env`(로컬) 또는 배포 환경의 시크릿 매커니즘을 사용하세요.

---
문의/개선 제안은 이슈로 남겨주세요. 브랜치 전략이나 컨트리뷰션 가이드가 필요하면 추가 작성할 수 있습니다.