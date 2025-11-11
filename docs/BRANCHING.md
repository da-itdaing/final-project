# Branching & CI 가이드

이 레포는 프론트엔드와 백엔드를 분리된 브랜치로 관리하면서, `dev/integration` 브랜치에서 풀스택 통합 검증을 진행하는 구조를 사용합니다.

## 브랜치 전략

* `main` — 프로덕션 배포용 (보호 브랜치)
* `dev/integration` — 통합 / 스테이징용 (보호 브랜치)
* `fe` — 프론트엔드 전용 브랜치 (`itdaing-web/` 하위 내용만 포함)
* `be` (또는 `dev/be`) — 백엔드 전용 브랜치
* `feature/<name>`, `fix/<name>` — `fe` 또는 `be` 브랜치에서 파생되는 기능/버그 수정 브랜치
* `hotfix/<name>` — 프로덕션을 우선(`main`)으로 고치고, 이후 `dev/integration`으로 역머지

---

## 최초 분리 작업 (1회만 수행)

처음 한 번만 프론트엔드를 분리하기 위한 작업입니다.

```bash
# dev/integration 최신 상태로 맞추기
git checkout dev/integration && git pull --ff-only

# itdaing-web subtree 히스토리 기준으로 fe 브랜치 생성
git subtree split -P itdaing-web -b fe

# 원격에 fe 브랜치 푸시
git push origin fe
```

이후 `be`/`dev/be` 브랜치에 `itdaing-web/` 폴더가 그대로 남아 있다면, 아래처럼 제거합니다.

```bash
git checkout be
git rm -r --cached itdaing-web
git commit -m "chore: remove frontend folder from backend branch"
git push origin be
```

---

## 일상적인 동기화(Daily Sync)

### 1) integration 변경사항을 `fe`로 가져오기

```bash
git checkout fe
git pull --ff-only

# dev/integration의 itdaing-web/ 내용을 fe로 가져오기
git subtree pull -P itdaing-web origin dev/integration -m "sync: from integration" --squash
```

### 2) 프론트엔드 변경사항을 `dev/integration`으로 반영하기

```bash
git checkout dev/integration
git pull --ff-only

# (처음 한 번만 실행)
git subtree add  -P itdaing-web origin fe --squash

# 이후부터는 아래 명령으로 갱신
git subtree pull -P itdaing-web origin fe -m "merge fe into integration" --squash
```

### 기본 규칙

* 프론트엔드 코드는 원칙적으로 `dev/integration`이 아닌 `fe` 브랜치에서만 수정합니다.
  (긴급 상황에서 `dev/integration`에 직접 커밋했다면, 나중에 반드시 `fe` 브랜치와 동기화해 주세요.)
* `dev/integration`으로 들어가는 PR은 **프론트엔드 전용**이나 **백엔드 전용** 중 하나로 유지하고, 한 PR에 둘을 섞지 않습니다.

---

## CI 개요

* FE CI (`.github/workflows/fe-ci.yml`)

  * `itdaing-web/` 경로 기준으로 eslint, tsc, vitest, vite build 수행

* BE CI (`.github/workflows/be-ci.yml`)

  * JDK 21 환경에서 Gradle 테스트 수행

* Contract Check (`.github/workflows/contract-check.yml`)

  * SpringDoc 플러그인으로 OpenAPI 스펙을 생성하고, Redocly로 기준 브랜치와 diff 수행

---

## OpenAPI → TS Client (Frontend)

백엔드에서 OpenAPI 스펙을 생성한 뒤(`./gradlew openApi`), 프론트엔드 타입 안전 클라이언트를 생성합니다.

```bash
pnpm gen:api  # itdaing-web/src/api 아래에 클라이언트 생성
```

* 입력: `../build/openapi/openapi.yaml`
* 출력: axios 기반의 타입이 포함된 클라이언트 코드

---

## Subtree 관련 예외 상황

### 1) integration에서 가져올 때 충돌 발생

```bash
git checkout fe
git subtree pull -P itdaing-web origin dev/integration -m "pre-sync from integration"
# 여기서 충돌을 해결하고 커밋

git checkout dev/integration
git subtree pull -P itdaing-web origin fe -m "sync fe → integration"
```

### 2) 실수로 `dev/integration`에서 `itdaing-web/`을 삭제한 경우

```bash
git checkout dev/integration
git subtree add -P itdaing-web origin fe --squash
```

### 3) 히스토리가 너무 무거워진 경우

* subtree 작업 시 `--squash` 옵션을 적극적으로 사용해서 커밋 히스토리를 단순화합니다.

---

## PR 작성 규칙(PR Hygiene)

* PR 제목 접두사:

  * `[FE]`, `[BE]`, `[CONTRACT]`, `[HOTFIX]`
* 변경 라인은 가능하면 ±300줄 내외로 유지
* PR 템플릿 내 체크박스:

  * OpenAPI 스펙 변경 여부
  * DB 마이그레이션(스키마 변경) 여부
