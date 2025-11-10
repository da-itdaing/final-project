# 팀 공통 문서

팀 전체가 공유하는 개발 프로세스 및 컨벤션 문서입니다.

## 📚 문서 목록

### 개발 프로세스
- **[BRANCHING.md](./BRANCHING.md)** - 브랜치 전략 및 Git 워크플로우
  - 브랜치 구조 (fe/be/dev/integration/main)
  - Git Subtree 사용법
  - 브랜치 동기화 방법
  - 충돌 해결 가이드

- **[BRANCH_PROTECTION.md](./BRANCH_PROTECTION.md)** - GitHub 브랜치 보호 규칙
  - PR 필수화 설정
  - 코드 리뷰 프로세스
  - CI 상태 체크 필수화
  - 권장 설정 및 FAQ

### 개발 환경
- **[IDE_SETUP.md](./IDE_SETUP.md)** - IDE 설정 가이드
  - IntelliJ IDEA 설정
  - VS Code 설정
  - 플러그인 추천
  - 코드 스타일 설정

## 🌳 브랜치 전략

### 브랜치 구조
```
main (프로덕션)
  ↑
dev/integration (스테이징)
  ↑         ↑
dev/be    dev/fe
  ↑         ↑
feature/  feature/
백엔드     프론트엔드
```

### 주요 브랜치
- **`main`**: 프로덕션 배포 브랜치 (보호됨)
- **`dev/integration`**: 통합 테스트 브랜치 (보호됨)
- **`dev/be`**: 백엔드 개발 브랜치
- **`dev/fe`**: 프론트엔드 개발 브랜치 (독립 저장소)
- **`fe`**: 프론트엔드 전용 히스토리 브랜치

## 🔄 개발 워크플로우

### 1. 기능 개발
```bash
# 1. 최신 코드 가져오기
git checkout dev/integration
git pull origin dev/integration

# 2. 기능 브랜치 생성
git checkout -b feature/new-feature

# 3. 개발 및 커밋 (gitmoji 사용)
git add .
git commit -m "✨ feat: 새 기능 추가"

# 4. 리모트에 푸시
git push origin feature/new-feature

# 5. GitHub에서 PR 생성
# Base: dev/integration
# Compare: feature/new-feature
```

### 2. 코드 리뷰
- 최소 1명의 팀원 승인 필요
- 모든 CI 체크 통과 확인
- 코멘트 해결 후 머지

### 3. 배포
- `dev/integration` → 스테이징 자동 배포
- `main` → 프로덕션 자동 배포

## 📋 커밋 컨벤션

### Gitmoji 사용
```
✨ :sparkles: - 새 기능 추가
🐛 :bug: - 버그 수정
📝 :memo: - 문서 작성/수정
♻️ :recycle: - 코드 리팩토링
✅ :white_check_mark: - 테스트 추가/수정
🔧 :wrench: - 설정 파일 수정
🚀 :rocket: - 배포 관련
🎨 :art: - 코드 포맷/구조 개선
⚡ :zap: - 성능 개선
🔥 :fire: - 코드/파일 삭제
```

### 커밋 메시지 구조
```
{gitmoji} {type}: {subject}

{body (선택사항)}

{footer (선택사항)}
```

### 예시
```
✨ feat: 사용자 회원가입 API 추가

- POST /api/auth/signup/consumer 엔드포인트 구현
- Bean Validation 적용
- 201 Created 상태 코드 반환

Closes #123
```

## 🎯 PR 가이드

### PR 제목
```
[타입] 간단한 설명

예시:
[Feature] 팝업 검색 기능 추가
[Bugfix] 로그인 세션 만료 이슈 수정
[Docs] API 문서 업데이트
```

### PR 템플릿
```markdown
## 변경 사항
- 무엇을 변경했는지 간단히 설명

## 관련 이슈
- Closes #123

## 체크리스트
- [ ] 테스트 추가/수정 완료
- [ ] 문서 업데이트 완료
- [ ] 코드 리뷰 가능한 상태
- [ ] CI 통과 확인
```

## 🧪 테스트 가이드

### 테스트 작성 원칙
- 모든 기능에 단위 테스트 작성
- 중요 플로우는 통합 테스트 작성
- 테스트 커버리지 유지

### 테스트 실행
```bash
# 백엔드 테스트
./gradlew test

# 프론트엔드 테스트
cd itdaing-web
pnpm test
```

## 📖 코드 리뷰 가이드

### 리뷰어 역할
- [ ] 코드가 요구사항을 충족하는가?
- [ ] 테스트가 충분한가?
- [ ] 코드가 읽기 쉬운가?
- [ ] 성능 이슈는 없는가?
- [ ] 보안 취약점은 없는가?

### 리뷰이 역할
- [ ] 변경 사항을 명확히 설명
- [ ] 테스트 결과 첨부
- [ ] 스크린샷/GIF 첨부 (UI 변경 시)
- [ ] 피드백에 대한 답변

### 코드 리뷰 에티켓
- 🙂 건설적인 피드백
- 🎯 구체적인 제안
- 💬 질문형으로 작성
- ✅ 좋은 점도 언급

**좋은 예시:**
```
Q: 이 부분에서 N+1 문제가 발생할 수 있을 것 같은데, 
   fetch join을 사용하는 것은 어떨까요?
```

**나쁜 예시:**
```
❌ 이 코드는 성능이 안 좋습니다.
```

## 🔧 개발 환경 설정

### 필수 도구
- **Java**: OpenJDK 21
- **Node.js**: v20 이상
- **pnpm**: v8 이상
- **Git**: 최신 버전

### IDE 설정
- IntelliJ IDEA: [IDE_SETUP.md](./IDE_SETUP.md) 참고
- VS Code: ESLint, Prettier 플러그인 설치

### 코드 스타일
- 백엔드: Google Java Style Guide + Spotless
- 프론트엔드: ESLint + Prettier

## 📅 회의 및 협업

### 정기 회의
- **데일리 스탠드업**: 매일 오전 10시 (10분)
  - 어제 한 일
  - 오늘 할 일
  - 블로커

- **스프린트 리뷰**: 매주 금요일 오후 3시 (1시간)
  - 완료된 작업 데모
  - 회고

### 커뮤니케이션 채널
- **Slack**:
  - `#general`: 일반 공지
  - `#dev-backend`: 백엔드 논의
  - `#dev-frontend`: 프론트엔드 논의
  - `#devops`: 배포 관련
  - `#emergency`: 긴급 장애

## 🔗 외부 리소스

### 학습 자료
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev/)
- [Git Subtree Tutorial](https://www.atlassian.com/git/tutorials/git-subtree)

### 참고 프로젝트
- [Spring Petclinic](https://github.com/spring-projects/spring-petclinic)
- [Real World App](https://github.com/gothinkster/realworld)

## 📞 팀 연락처

- **팀 리드**: @team-lead
- **백엔드 리드**: @backend-lead  
- **프론트엔드 리드**: @frontend-lead
- **DevOps**: @devops-lead

## 📝 문서 작성 가이드

### 새 문서 추가 시
1. 적절한 폴더에 배치
   - `docs/backend/`: 백엔드 관련
   - `docs/frontend/`: 프론트엔드 관련
   - `docs/deploy/`: 배포 관련
   - `docs/team/`: 팀 공통

2. README.md에 문서 링크 추가

3. PR로 팀원들과 공유

### 문서 작성 원칙
- 명확하고 간결하게
- 코드 예시 포함
- 스크린샷 활용
- 정기적으로 업데이트
