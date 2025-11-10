# Frontend 문서

프론트엔드 개발자를 위한 문서 모음입니다.

## 📚 문서 목록

> 현재 프론트엔드 문서가 준비 중입니다. 아래 내용을 참고하세요.

## 🚀 빠른 시작

### 개발 환경 설정
```bash
cd itdaing-web

# 의존성 설치
pnpm install

# 개발 서버 실행
pnpm dev

# 브라우저에서 확인
# http://localhost:5173
```

### 빌드 및 테스트
```bash
# 린트 체크
pnpm lint

# 타입 체크
pnpm typecheck

# 테스트 실행
pnpm test

# 프로덕션 빌드
pnpm build
```

## 🔗 API 연동

### OpenAPI 스펙으로 타입 생성
```bash
# 백엔드 서버 실행 후
cd itdaing-web
pnpm gen:api
```

이 명령은 다음을 자동 생성합니다:
- TypeScript 타입 정의
- API 클라이언트 함수
- 위치: `src/api/` 폴더

### API 엔드포인트
- **로컬 개발**: `http://localhost:8080`
- **스테이징**: `http://dev-api.itdaing.com` (예시)
- **프로덕션**: `https://api.itdaing.com` (예시)

## 📋 개발 가이드

### 폴더 구조
```
itdaing-web/
├── public/          # 정적 파일
├── src/
│   ├── components/  # 재사용 컴포넌트
│   ├── pages/       # 페이지 컴포넌트
│   ├── api/         # API 클라이언트 (자동 생성)
│   ├── types/       # 타입 정의
│   ├── styles/      # 스타일 파일
│   ├── context/     # React Context
│   ├── data/        # 더미 데이터
│   └── __tests__/   # 테스트 파일
├── vite.config.ts   # Vite 설정
├── vitest.config.ts # Vitest 설정
└── package.json
```

### 코드 스타일
- ESLint 설정 준수
- TypeScript strict 모드 사용
- 컴포넌트는 명명된 함수로 작성
- CSS Modules 또는 Styled Components 사용 (선택)

### 테스트 작성
```typescript
// src/__tests__/example.test.ts
import { describe, it, expect } from 'vitest';

describe('컴포넌트명', () => {
  it('기능 설명', () => {
    expect(true).toBe(true);
  });
});
```

## 🔄 백엔드 연동 플로우

### 1. API 스펙 확인
```bash
# Swagger UI 접속
open http://localhost:8080/swagger-ui.html
```

### 2. 타입 생성
```bash
pnpm gen:api
```

### 3. API 호출 예시
```typescript
import { getUsers } from '@/api';

async function fetchUsers() {
  try {
    const response = await getUsers();
    console.log(response.data);
  } catch (error) {
    console.error('사용자 조회 실패:', error);
  }
}
```

## 🎨 디자인 시스템

> 디자인 시스템 문서 작성 예정

### 주요 컴포넌트
- Button
- Input
- Card
- Modal
- Layout

## 📱 반응형 디자인

### 브레이크포인트
```css
/* 모바일 */
@media (max-width: 767px) { }

/* 태블릿 */
@media (min-width: 768px) and (max-width: 1023px) { }

/* 데스크톱 */
@media (min-width: 1024px) { }
```

## 🔍 문제 해결

### 개발 서버가 시작되지 않을 때
```bash
# node_modules 재설치
rm -rf node_modules pnpm-lock.yaml
pnpm install

# 캐시 클리어
pnpm store prune
```

### 타입 에러가 발생할 때
```bash
# 타입 체크 실행
pnpm typecheck

# API 타입 재생성
pnpm gen:api
```

### 빌드 에러가 발생할 때
1. `package.json` 버전 확인
2. Node.js 버전 확인 (권장: v20 이상)
3. pnpm 버전 확인

## 🔗 관련 리소스

### 팀 문서
- IDE 설정: [`docs/team/IDE_SETUP.md`](../team/IDE_SETUP.md)
- 브랜치 전략: [`docs/team/BRANCHING.md`](../team/BRANCHING.md)
- 브랜치 보호: [`docs/team/BRANCH_PROTECTION.md`](../team/BRANCH_PROTECTION.md)

### 백엔드 API
- API 문서: [`docs/backend/`](../backend/)
- OpenAPI 스펙: `docs/openapi.json`

### 라이브러리
- [Vite](https://vitejs.dev/)
- [React](https://react.dev/)
- [TypeScript](https://www.typescriptlang.org/)
- [Vitest](https://vitest.dev/)

## 📝 작성 예정 문서

- [ ] 컴포넌트 가이드
- [ ] 상태 관리 전략
- [ ] 라우팅 설정
- [ ] 환경 변수 설정
- [ ] 배포 가이드
- [ ] 성능 최적화
