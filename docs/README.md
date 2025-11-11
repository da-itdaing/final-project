# 📚 itdaing 프로젝트 문서

전체 프로젝트 문서의 중앙 인덱스입니다. 역할에 맞는 문서를 찾아보세요.

## 🗂️ 문서 구조

```
docs/
├── README.md (이 파일)
├── backend/          # 백엔드 개발자용 문서
├── frontend/         # 프론트엔드 개발자용 문서
├── deploy/           # 배포 및 인프라 문서
├── team/             # 팀 공통 문서
└── schema/           # 데이터베이스 스키마
```

---

## 👥 역할별 문서 가이드

### 🎨 프론트엔드 개발자
**시작하기**: [`frontend/README.md`](./frontend/README.md)

주요 문서:
- 개발 환경 설정
- API 연동 가이드
- 컴포넌트 가이드
- 빌드 및 테스트

**팀 공통 문서**도 함께 확인하세요:
- [브랜치 전략](./team/BRANCHING.md)
- [IDE 설정](./team/IDE_SETUP.md)

---

### ⚙️ 백엔드 개발자
**시작하기**: [`backend/README.md`](./backend/README.md)

주요 문서:
- [테스트 수정 내역](./backend/TEST_FIXES.md) ⭐ 필독
- [JWT 인증 구현](./backend/JWT_AUTH_IMPLEMENTATION_SUMMARY.md)
- [도메인 테스트](./backend/DOMAIN_TESTS.md)
- [API 문서화](./backend/AUTH_SWAGGER_UPDATE_SUMMARY.md)

**팀 공통 문서**도 함께 확인하세요:
- [브랜치 전략](./team/BRANCHING.md)
- [브랜치 보호 규칙](./team/BRANCH_PROTECTION.md)
- [IDE 설정](./team/IDE_SETUP.md)

---

### 🚀 DevOps / 배포 담당자
**시작하기**: [`deploy/README.md`](./deploy/README.md)

주요 문서:
- [EC2 배포 가이드](./deploy/DEPLOY_EC2.md)
- 환경 변수 설정
- 모니터링 및 로그
- 트러블슈팅

**팀 공통 문서**도 함께 확인하세요:
- [브랜치 전략](./team/BRANCHING.md) (배포 플로우 이해 필수)

---

### 👨‍💼 팀 리드 / PM
**시작하기**: [`team/README.md`](./team/README.md)

주요 문서:
- [브랜치 전략](./team/BRANCHING.md) ⭐ 필독
- [브랜치 보호 규칙](./team/BRANCH_PROTECTION.md) ⭐ 설정 필수
- 커밋 컨벤션
- PR 가이드
- 코드 리뷰 가이드

---

## 🚀 빠른 시작

### 첫 시작하는 팀원
1. **팀 공통 문서 읽기** → [`team/README.md`](./team/README.md)
2. **IDE 설정** → [`team/IDE_SETUP.md`](./team/IDE_SETUP.md)
3. **브랜치 전략 이해** → [`team/BRANCHING.md`](./team/BRANCHING.md)
4. **역할별 문서 확인** → backend/ 또는 frontend/

### 기여하고 싶은 팀원
1. **브랜치 생성** → `feature/your-feature-name`
2. **PR 생성** → 템플릿 작성
3. **코드 리뷰** → 최소 1명 승인
4. **머지** → CI 통과 확인

---

## 📋 전체 문서 목록

### 🎨 Frontend
- [`frontend/README.md`](./frontend/README.md) - 프론트엔드 개발 가이드

### ⚙️ Backend
- [`backend/README.md`](./backend/README.md) - 백엔드 개발 가이드
- [`backend/TEST_FIXES.md`](./backend/TEST_FIXES.md) - 테스트 수정 내역 ⭐
- [`backend/DOMAIN_TESTS.md`](./backend/DOMAIN_TESTS.md) - 도메인 테스트 가이드
- [`backend/JWT_AUTH_IMPLEMENTATION_SUMMARY.md`](./backend/JWT_AUTH_IMPLEMENTATION_SUMMARY.md) - JWT 인증 구현
- [`backend/AUTHCONTROLLER_RECOVERY_REPORT.md`](./backend/AUTHCONTROLLER_RECOVERY_REPORT.md) - 인증 컨트롤러 복구 보고서
- [`backend/AUTH_SWAGGER_UPDATE_SUMMARY.md`](./backend/AUTH_SWAGGER_UPDATE_SUMMARY.md) - Swagger 문서 업데이트
- [`backend/CONTROLLER_AUDIT_EMPTY_BODY_REPORT.md`](./backend/CONTROLLER_AUDIT_EMPTY_BODY_REPORT.md) - 빈 응답 이슈 보고서

### 🚀 Deploy
- [`deploy/README.md`](./deploy/README.md) - 배포 가이드
- [`deploy/DEPLOY_EC2.md`](./deploy/DEPLOY_EC2.md) - EC2 배포 상세 가이드

### 👥 Team
- [`team/README.md`](./team/README.md) - 팀 공통 가이드
- [`team/BRANCHING.md`](./team/BRANCHING.md) - 브랜치 전략 ⭐
- [`team/BRANCH_PROTECTION.md`](./team/BRANCH_PROTECTION.md) - 브랜치 보호 규칙 ⭐
- [`team/IDE_SETUP.md`](./team/IDE_SETUP.md) - IDE 설정 가이드

### 📊 Schema
- [`schema/dbdiagram_v2.dbml`](./schema/dbdiagram_v2.dbml) - 데이터베이스 스키마

---

## 🔍 자주 찾는 문서

### "테스트가 실패해요!"
→ [`backend/TEST_FIXES.md`](./backend/TEST_FIXES.md) 확인

### "브랜치를 어떻게 관리하나요?"
→ [`team/BRANCHING.md`](./team/BRANCHING.md) 확인

### "PR은 어떻게 만드나요?"
→ [`team/README.md`](./team/README.md)의 PR 가이드 섹션

### "배포는 어떻게 하나요?"
→ [`deploy/README.md`](./deploy/README.md) 확인

### "API 문서는 어디 있나요?"
→ 개발 서버 실행 후 `http://localhost:8080/swagger-ui.html`

### "프론트엔드 타입은 어떻게 생성하나요?"
→ [`frontend/README.md`](./frontend/README.md)의 API 연동 섹션

---

## 📝 문서 작성 규칙

### 새 문서 추가 시
1. **적절한 폴더에 배치**
   - 백엔드 관련 → `backend/`
   - 프론트엔드 관련 → `frontend/`
   - 배포 관련 → `deploy/`
   - 팀 공통 → `team/`

2. **해당 폴더의 README.md에 링크 추가**

3. **이 문서(docs/README.md)에도 링크 추가**

4. **PR로 팀원들과 공유**

### 문서 작성 원칙
- ✅ 명확하고 간결하게
- ✅ 코드 예시 포함
- ✅ 스크린샷 활용 (UI 관련)
- ✅ 정기적으로 업데이트

---

## 🔗 외부 리소스

### 프로젝트 리소스
- **GitHub Repository**: [da-itdaing/final-project](https://github.com/da-itdaing/final-project)
- **API 문서**: http://localhost:8080/swagger-ui.html (로컬)
- **스테이징**: https://dev.itdaing.com (예시)
- **프로덕션**: https://itdaing.com (예시)

### 기술 스택 문서
- [Spring Boot](https://spring.io/projects/spring-boot)
- [React](https://react.dev/)
- [Vite](https://vitejs.dev/)
- [Vitest](https://vitest.dev/)

---

## 📞 도움이 필요하신가요?

### Slack 채널
- `#general`: 일반 공지 및 논의
- `#dev-backend`: 백엔드 개발 관련
- `#dev-frontend`: 프론트엔드 개발 관련
- `#devops`: 배포 및 인프라
- `#emergency`: 긴급 장애 대응

### 팀 연락처
- **팀 리드**: @team-lead
- **백엔드 리드**: @backend-lead
- **프론트엔드 리드**: @frontend-lead
- **DevOps**: @devops-lead

---

## 📅 문서 업데이트 이력

| 날짜 | 내용 | 작성자 |
|------|------|--------|
| 2025-11-11 | 문서 구조 개편 (역할별 분류) | @team |
| 2025-11-11 | 테스트 수정 내역 추가 | @team |
| 2025-11-11 | 브랜치 보호 규칙 추가 | @team |

---

## ⭐ 중요 공지

### 필독 문서 (신규 팀원)
1. [`team/BRANCHING.md`](./team/BRANCHING.md) - 브랜치 전략
2. [`team/BRANCH_PROTECTION.md`](./team/BRANCH_PROTECTION.md) - 보호 규칙
3. [`backend/TEST_FIXES.md`](./backend/TEST_FIXES.md) (백엔드)
4. [`frontend/README.md`](./frontend/README.md) (프론트엔드)

### 최근 업데이트
- ✅ 전체 테스트 스위트 통과 (115 tests)
- ✅ CI/CD 파이프라인 구축
- ✅ 브랜치 보호 규칙 문서화
- 🚧 프론트엔드 문서 작성 중

---

**Happy Coding! 🚀**
