
# itdaing-web (frontend)

[![CI](https://github.com/da-itdaing/final-project/actions/workflows/ci.yml/badge.svg?branch=dev/integration)](https://github.com/da-itdaing/final-project/actions/workflows/ci.yml)

## Quick start

1) Install dependencies

```bash
npm i
```

2) Start dev server

```bash
npm run dev
```

3) Build

```bash
npm run build
```

## CI behavior

- dev/integration 브랜치에 push 또는 PR이 올라오면 GitHub Actions 가 자동으로 프론트엔드/백엔드를 각각 빌드합니다.
- 프론트엔드: Node 20 + Vite 빌드 → `dist/` 산출물 아티팩트 업로드
- 백엔드: JDK 21 + Gradle `bootJar -x test` (테스트는 CI 기본 단계에서 스킵)

## Commit 메시지 (gitmoji)

- 커밋 메시지 맨 앞에 gitmoji를 사용합니다. 예)
  - ✨ feat: 캐러셀 반응형 개선
  - 🐛 fix: 모바일 우측 overflow 수정
  - 🔧 chore: CI 스크립트 정리

간편하게는 gitmoji-cli를 사용할 수 있습니다:

```bash
npx gitmoji -c
```

또는 직접 이모지/코드를 붙여서 커밋해도 됩니다. 예: `git commit -m ":sparkles: feat: add hero carousel breakpoints"`

## PWA 기능

- 설치 가능: manifest(`site.webmanifest`) + service worker(`public/sw.js`) 등록으로 홈 화면 추가 가능
- 오프라인 처리: 네비게이션 실패 시 `offline.html`을 표시
- 캐시 전략: 앱 셸(정적 아이콘/HTML/manifest)은 install 시 선 캐시, 나머지 요청은 Cache First + 네트워크 후 런타임 캐시 저장
- 버전 변경: `sw.js` 내부 `VERSION` 수정 후 다시 빌드/배포하면 오래된 캐시 자동 제거

### 로컬 테스트 방법
1. 개발 모드에서는 service worker가 등록되지 않습니다 (`import.meta.env.PROD` 조건).
2. 프로덕션 빌드 후 로컬 프리뷰:
```bash
npm run build
npm run preview
```
3. 브라우저 DevTools > Application > Service Workers 에서 등록 확인 후
  - Network 탭에서 "Offline" 체크 → 새 탭에서 페이지 이동 시 `offline.html` 응답 확인
4. 홈 화면 추가(Android Chrome): 메뉴 → "앱 설치" (Add to Home screen)

### 캐시 무효화
배포 후 아이콘/코드 변경이 갱신 안 되면:
1) `sw.js`의 VERSION 값을 증가 → 다시 빌드/배포
2) 사용자가 브라우저 DevTools > Application > Service Workers > unregister 후 새로고침

### 향후 확장 아이디어
- Precache 주요 라우트별 코드 스플릿 청크
- 백엔드 API 응답(팝업 목록 등)에 대한 Stale-While-Revalidate 전략 추가
- Push 알림 (FCM) 연계 및 Background Sync
