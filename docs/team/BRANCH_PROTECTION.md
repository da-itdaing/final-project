# GitHub 브랜치 보호 규칙 설정 가이드

> 작성일: 2025-11-11  
> 목적: main, dev/integration 브랜치의 품질과 안정성 보장

## 📌 개요

브랜치 보호 규칙(Branch Protection Rules)을 설정하여:
- ✅ 코드 리뷰 없이 직접 푸시 방지
- ✅ CI/CD 테스트 통과를 필수로 설정
- ✅ 강제 푸시(force push) 차단
- ✅ 브랜치 삭제 방지

---

## 🎯 보호 대상 브랜치

### 1. `main` 브랜치
- **용도**: 프로덕션 배포 브랜치
- **머지 소스**: `dev/integration` 브랜치만 머지 허용
- **배포**: 자동 배포 (CD 파이프라인)

### 2. `dev/integration` 브랜치
- **용도**: 통합 테스트 및 스테이징 환경
- **머지 소스**: `dev/be`, `dev/fe` 브랜치에서 머지
- **배포**: 스테이징 서버 자동 배포

---

## ⚙️ 설정 방법 (단계별)

### Step 1: GitHub Repository 설정 페이지 접속

1. GitHub에서 저장소 페이지로 이동
2. **Settings** 탭 클릭
3. 왼쪽 메뉴에서 **Branches** 선택

### Step 2: Branch Protection Rule 추가

1. **Add branch protection rule** 버튼 클릭
2. **Branch name pattern** 입력:
   - `main` (프로덕션)
   - `dev/integration` (스테이징)

---

## 🔐 권장 설정 (main 브랜치)

### 1. Pull Request 필수화
```
☑️ Require a pull request before merging
   ☑️ Require approvals (최소 승인 수: 1명)
   ☑️ Dismiss stale pull request approvals when new commits are pushed
   ☑️ Require review from Code Owners (선택사항)
```

**설명**:
- 모든 변경사항은 PR을 통해서만 머지 가능
- 최소 1명의 팀원이 코드 리뷰 및 승인 필요
- 새 커밋 푸시 시 이전 승인 무효화

### 2. 상태 체크 필수화
```
☑️ Require status checks to pass before merging
   ☑️ Require branches to be up to date before merging
   
   필수 상태 체크 항목:
   - ✅ be-ci (백엔드 CI: spotlessCheck + test)
   - ✅ contract-check (OpenAPI 계약 검증)
```

**설명**:
- 백엔드 테스트가 통과해야만 머지 가능
- API 계약 변경이 breaking change가 아닌지 검증
- 머지 전 최신 코드로 업데이트 필수

### 3. 커밋 히스토리 보호
```
☑️ Require linear history (선택사항)
☐ Require signed commits (선택사항, 보안 강화)
```

**설명**:
- 깔끔한 커밋 히스토리 유지
- Squash and merge 또는 Rebase and merge 사용 권장

### 4. 강제 푸시 차단
```
☑️ Do not allow bypassing the above settings
☐ Allow force pushes (체크 해제 권장)
☐ Allow deletions (체크 해제 권장)
```

**설명**:
- 관리자를 포함한 모든 사용자에게 규칙 적용
- force push로 인한 커밋 손실 방지
- 실수로 브랜치 삭제 방지

### 5. 추가 보호 설정
```
☑️ Require conversation resolution before merging
☑️ Lock branch (프로덕션 배포 중 일시적으로 활성화 가능)
```

**설명**:
- PR의 모든 코멘트가 해결되어야 머지 가능
- 긴급 상황 시 브랜치 잠금 가능

---

## 🧪 권장 설정 (dev/integration 브랜치)

### 1. Pull Request 필수화
```
☑️ Require a pull request before merging
   ☑️ Require approvals (최소 승인 수: 1명)
   ☐ Dismiss stale pull request approvals (선택사항)
```

**main과의 차이점**:
- 개발 속도를 위해 stale approval 무효화는 선택사항

### 2. 상태 체크 필수화
```
☑️ Require status checks to pass before merging
   ☑️ Require branches to be up to date before merging
   
   필수 상태 체크 항목:
   - ✅ fe-ci (프론트엔드 CI: lint + typecheck + test + build)
   - ✅ be-ci (백엔드 CI: spotlessCheck + test)
   - ✅ contract-check (OpenAPI 계약 검증)
```

**설명**:
- 프론트엔드와 백엔드 모두 테스트 통과 필수
- 통합 환경이므로 양쪽 모두 검증

### 3. 강제 푸시 차단
```
☑️ Do not allow bypassing the above settings
☐ Allow force pushes (체크 해제)
☐ Allow deletions (체크 해제)
```

---

## 📋 설정 완료 확인 체크리스트

### main 브랜치
- [ ] Branch protection rule 생성 완료
- [ ] PR 필수화 (최소 1명 승인)
- [ ] 상태 체크 필수: `be-ci`, `contract-check`
- [ ] Force push 차단
- [ ] 브랜치 삭제 방지
- [ ] Bypass 권한 없음 확인

### dev/integration 브랜치
- [ ] Branch protection rule 생성 완료
- [ ] PR 필수화 (최소 1명 승인)
- [ ] 상태 체크 필수: `fe-ci`, `be-ci`, `contract-check`
- [ ] Force push 차단
- [ ] 브랜치 삭제 방지
- [ ] Bypass 권한 없음 확인

---

## 🧑‍💻 팀 역할 및 권한

### Repository Roles

#### Admin (저장소 소유자)
- 브랜치 보호 규칙 설정/수정 가능
- **주의**: Admin도 규칙을 bypass할 수 없도록 설정 권장

#### Maintainer
- PR 승인 및 머지 권한
- 브랜치 생성/삭제 권한

#### Write (개발자)
- PR 생성 권한
- 자신의 브랜치에 push 권한
- 보호된 브랜치에는 직접 push 불가

---

## 🔄 워크플로우 예시

### 기능 개발 플로우 (백엔드)

```
1. 로컬에서 feature 브랜치 생성
   git checkout -b feature/add-payment-api

2. 개발 및 커밋
   git add .
   git commit -m "✨ feat: 결제 API 추가"

3. 리모트에 푸시
   git push origin feature/add-payment-api

4. GitHub에서 PR 생성
   - Base: dev/integration
   - Compare: feature/add-payment-api
   - Reviewers: 팀원 지정

5. CI 자동 실행 대기
   - ✅ be-ci (spotlessCheck + test)
   - ✅ contract-check (OpenAPI diff)

6. 코드 리뷰 진행
   - 팀원이 코멘트 작성
   - 수정 사항 반영 후 재푸시
   - 모든 conversation 해결

7. 승인 및 머지
   - 최소 1명 승인 완료
   - CI 모두 통과
   - "Squash and merge" 버튼 클릭

8. 브랜치 정리
   - 머지 후 feature 브랜치 자동 삭제
```

### 프로덕션 배포 플로우

```
1. dev/integration → main PR 생성
   - Title: "Release v1.2.0"
   - Description: 배포 내용 요약

2. CI 실행 확인
   - ✅ be-ci
   - ✅ contract-check

3. 최종 리뷰 및 승인
   - 팀 리드 또는 시니어 개발자 승인 필요

4. 머지 및 배포
   - "Create a merge commit" 사용 (히스토리 보존)
   - 자동 배포 트리거
```

---

## 🚨 긴급 상황 대응

### Hotfix가 필요한 경우

```bash
# 1. main 브랜치에서 hotfix 브랜치 생성
git checkout main
git pull origin main
git checkout -b hotfix/critical-bug-fix

# 2. 버그 수정
# ... 코드 수정 ...
git commit -m "🐛 hotfix: 크리티컬 버그 수정"

# 3. 리모트 푸시
git push origin hotfix/critical-bug-fix

# 4. main으로 직접 PR (긴급)
# - Base: main
# - Compare: hotfix/critical-bug-fix
# - Label: "hotfix", "urgent"
# - 빠른 리뷰 요청

# 5. 머지 후 dev/integration에도 반영
git checkout dev/integration
git cherry-pick <hotfix-commit-hash>
git push origin dev/integration
```

### 브랜치 잠금이 필요한 경우

1. GitHub Settings → Branches
2. 해당 브랜치 protection rule 수정
3. **Lock branch** 체크
4. Save changes

> ⚠️ 배포 중이거나 중요한 작업 진행 시에만 일시적으로 사용

---

## 📊 효과 측정

브랜치 보호 규칙 적용 후 확인할 지표:

### 코드 품질
- [ ] PR당 평균 코멘트 수 증가
- [ ] 버그 발견 및 수정 시점이 프로덕션 이전으로 이동
- [ ] 테스트 커버리지 유지 또는 증가

### 프로세스
- [ ] main 브랜치의 직접 커밋 0건
- [ ] CI 실패로 인한 머지 차단 건수
- [ ] 코드 리뷰 참여도 증가

### 안정성
- [ ] 프로덕션 배포 후 롤백 횟수 감소
- [ ] 긴급 hotfix 빈도 감소
- [ ] API breaking change 사전 감지

---

## 🔗 관련 문서

- [BRANCHING.md](./BRANCHING.md) - 브랜치 전략 및 워크플로우
- [TEST_FIXES.md](./TEST_FIXES.md) - 테스트 수정 내역
- [GitHub Docs - Branch Protection](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-protected-branches/about-protected-branches)

---

## 💡 Tips

### 1. 초기 설정 시 점진적 적용
```
1주차: PR 필수화만 적용
2주차: 코드 리뷰 승인 추가
3주차: CI 상태 체크 필수화
```

### 2. 팀 컨벤션 문서화
- PR 템플릿 활용: `.github/pull_request_template.md`
- 코드 리뷰 가이드라인 작성
- 커밋 메시지 규칙 (gitmoji 등)

### 3. CI 실행 시간 최적화
- 테스트를 병렬로 실행
- Docker layer 캐싱 활용
- 변경된 모듈만 테스트 (선택적 실행)

### 4. 알림 설정
- GitHub 알림을 Slack과 연동
- PR 생성/승인/머지 시 팀 채널에 자동 알림
- CI 실패 시 담당자에게 멘션

---

## ❓ FAQ

### Q1. Admin도 보호 규칙을 bypass할 수 없나요?
**A**: "Do not allow bypassing the above settings" 체크 시 관리자도 규칙을 준수해야 합니다. 긴급 상황에는 일시적으로 규칙을 수정할 수 있습니다.

### Q2. CI가 오래 걸려서 개발 속도가 느려지는데요?
**A**: 
- 테스트를 unit/integration으로 분리하여 빠른 피드백
- PR 초안(draft) 기능 활용 (CI 실행 제외)
- 캐싱 최적화

### Q3. feature 브랜치도 보호해야 하나요?
**A**: 개인 작업 브랜치는 보호 불필요합니다. 장기 개발 브랜치(예: `dev/be`, `dev/fe`)는 선택적으로 보호 가능합니다.

### Q4. 리뷰어가 없어서 머지가 지연되는데요?
**A**: 
- 팀 내 코드 리뷰 시간 규칙 정하기 (예: 24시간 내)
- CODEOWNERS 파일로 자동 리뷰어 지정
- 리뷰어 부재 시 대체 리뷰어 지정 규칙 마련

### Q5. Squash merge vs Rebase merge vs Merge commit?
**A**:
- **Squash merge**: feature 브랜치 → dev/integration (권장)
  - 장점: 깔끔한 히스토리, PR 단위로 커밋 정리
- **Merge commit**: dev/integration → main (권장)
  - 장점: 릴리스 히스토리 명확, 롤백 용이
- **Rebase merge**: 선택사항
  - 장점: 선형 히스토리, 단점: 충돌 해결 복잡

---

## 📝 변경 이력

| 날짜 | 버전 | 변경 내용 |
|------|------|-----------|
| 2025-11-11 | 1.0.0 | 초안 작성 |
