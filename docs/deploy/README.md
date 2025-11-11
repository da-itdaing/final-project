# 배포 문서

DevOps 및 배포 관련 문서 모음입니다.

## 📚 문서 목록

- **[DEPLOY_EC2.md](./DEPLOY_EC2.md)** - EC2 배포 가이드
  - AWS EC2 인스턴스 설정
  - 애플리케이션 배포 스크립트
  - 환경 변수 설정
  - 트러블슈팅

## 🚀 배포 환경

### 인프라 구성
```
┌─────────────┐
│   GitHub    │
│  Repository │
└──────┬──────┘
       │ push to main/dev
       ↓
┌─────────────┐
│   GitHub    │
│   Actions   │ ← CI/CD 파이프라인
└──────┬──────┘
       │ deploy
       ↓
┌─────────────┐
│   AWS EC2   │
│  (Backend)  │
└─────────────┘
┌─────────────┐
│   AWS S3 +  │
│ CloudFront  │ ← Frontend (예정)
└─────────────┘
```

### 환경별 배포

#### 개발 환경 (dev/integration → Staging)
- **브랜치**: `dev/integration`
- **배포 시점**: PR 머지 후 자동 배포
- **서버**: EC2 스테이징 인스턴스
- **도메인**: `https://dev.itdaing.com` (예시)
- **목적**: 통합 테스트 및 QA

#### 프로덕션 환경 (main → Production)
- **브랜치**: `main`
- **배포 시점**: Release PR 머지 후 자동 배포
- **서버**: EC2 프로덕션 인스턴스
- **도메인**: `https://itdaing.com` (예시)
- **목적**: 실사용자 서비스

## 🔧 배포 프로세스

### 1. 자동 배포 (CI/CD)

#### GitHub Actions 워크플로우
```yaml
# .github/workflows/deploy-backend.yml
name: Deploy Backend

on:
  push:
    branches:
      - main
      - dev/integration

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v3
      
      - name: Setup Java
        uses: actions/setup-java@v3
        with:
          java-version: '21'
      
      - name: Build
        run: ./gradlew bootJar
      
      - name: Deploy to EC2
        run: |
          # SCP로 jar 파일 전송
          # SSH로 서버 재시작
```

### 2. 수동 배포

#### 백엔드 배포
```bash
# 1. 프로젝트 빌드
./gradlew clean bootJar

# 2. EC2로 전송
scp build/libs/itdaing-0.0.1-SNAPSHOT.jar ec2-user@YOUR_EC2_IP:/home/ec2-user/app/

# 3. 서버 접속 및 재시작
ssh ec2-user@YOUR_EC2_IP
cd /home/ec2-user/app
./deploy.sh
```

#### 프론트엔드 배포 (예정)
```bash
# 1. 빌드
cd itdaing-web
pnpm build

# 2. S3 업로드
aws s3 sync dist/ s3://itdaing-frontend/ --delete

# 3. CloudFront 캐시 무효화
aws cloudfront create-invalidation --distribution-id YOUR_DIST_ID --paths "/*"
```

## 🔐 환경 변수 설정

### 백엔드 환경 변수

#### application-prod.yml
```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  
  jwt:
    secret: ${JWT_SECRET}
    access-token-expiration: 3600000
    refresh-token-expiration: 1209600000

server:
  port: 8080
```

#### EC2 환경 변수 설정
```bash
# /etc/environment 또는 ~/.bashrc
export DB_URL=jdbc:mysql://rds-endpoint:3306/itdaing
export DB_USERNAME=admin
export DB_PASSWORD=secure_password
export JWT_SECRET=your_jwt_secret_key_here
```

### 프론트엔드 환경 변수

#### .env.production
```bash
VITE_API_BASE_URL=https://api.itdaing.com
VITE_APP_ENV=production
```

## 📊 모니터링

### 헬스 체크 엔드포인트
```bash
# 백엔드 헬스 체크
curl https://api.itdaing.com/actuator/health

# 응답 예시
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "diskSpace": { "status": "UP" }
  }
}
```

### 로그 확인
```bash
# EC2 접속
ssh ec2-user@YOUR_EC2_IP

# 애플리케이션 로그
tail -f /var/log/itdaing/application.log

# 시스템 로그
journalctl -u itdaing -f
```

## 🐛 트러블슈팅

### 배포 실패 시

#### 1. CI/CD 파이프라인 실패
```bash
# GitHub Actions 로그 확인
# Repository → Actions → 실패한 워크플로우 클릭

# 주요 확인 사항:
- ✅ 테스트 통과 여부
- ✅ 빌드 성공 여부
- ✅ AWS 자격 증명 확인
```

#### 2. EC2 서버 미응답
```bash
# 1. SSH 접속 확인
ssh ec2-user@YOUR_EC2_IP

# 2. 프로세스 확인
ps aux | grep java

# 3. 포트 확인
sudo netstat -tulpn | grep 8080

# 4. 서비스 재시작
sudo systemctl restart itdaing
```

#### 3. 데이터베이스 연결 실패
```bash
# RDS 보안 그룹 확인
# EC2 → RDS 간 3306 포트 오픈 확인

# 연결 테스트
mysql -h rds-endpoint -u admin -p
```

### 롤백 방법

#### 백엔드 롤백
```bash
# 1. 이전 버전 jar 파일 백업 확인
ls -l /home/ec2-user/app/backup/

# 2. 이전 버전으로 복구
cd /home/ec2-user/app
cp backup/itdaing-previous.jar itdaing.jar

# 3. 재시작
./deploy.sh
```

#### Git 롤백 (긴급)
```bash
# 1. 문제 커밋 확인
git log --oneline

# 2. Revert 커밋 생성
git revert <commit-hash>

# 3. 푸시 (자동 배포 트리거)
git push origin main
```

## 🔒 보안 체크리스트

### 배포 전 확인사항
- [ ] 민감 정보가 코드에 하드코딩되지 않았는지 확인
- [ ] 환경 변수로 관리되는지 확인
- [ ] HTTPS 설정 완료
- [ ] CORS 설정 확인
- [ ] 방화벽 규칙 확인
- [ ] 데이터베이스 백업 설정
- [ ] 로그 로테이션 설정

### AWS 보안 설정
- [ ] IAM 역할 최소 권한 원칙
- [ ] Security Group 최소 포트 오픈
- [ ] RDS 암호화 활성화
- [ ] S3 버킷 퍼블릭 액세스 차단
- [ ] CloudWatch 모니터링 활성화

## 📈 성능 최적화

### 백엔드 최적화
- Connection Pool 설정
- JVM 메모리 튜닝
- 쿼리 최적화 (N+1 문제 해결)
- Redis 캐싱 (예정)

### 프론트엔드 최적화
- 번들 사이즈 최소화
- Lazy Loading
- CDN 활용
- 이미지 최적화

## 📝 배포 체크리스트

### 스테이징 배포 전
- [ ] dev/integration 브랜치 최신 상태 확인
- [ ] 모든 테스트 통과 확인
- [ ] 코드 리뷰 완료
- [ ] API 계약 검증 (contract-check) 통과
- [ ] 환경 변수 설정 확인

### 프로덕션 배포 전
- [ ] 스테이징 환경 정상 동작 확인
- [ ] 릴리스 노트 작성
- [ ] 데이터베이스 마이그레이션 준비
- [ ] 롤백 계획 수립
- [ ] 모니터링 알림 설정 확인
- [ ] 팀원에게 배포 공지

### 배포 후
- [ ] 헬스 체크 엔드포인트 확인
- [ ] 주요 API 동작 확인
- [ ] 로그 모니터링
- [ ] 에러율 확인
- [ ] 응답 속도 확인

## 🔗 관련 리소스

### AWS 리소스
- EC2 대시보드: [AWS Console](https://console.aws.amazon.com/ec2/)
- RDS 대시보드: [AWS Console](https://console.aws.amazon.com/rds/)
- CloudWatch 로그: [AWS Console](https://console.aws.amazon.com/cloudwatch/)

### 팀 문서
- 브랜치 전략: [`docs/team/BRANCHING.md`](../team/BRANCHING.md)
- 브랜치 보호: [`docs/team/BRANCH_PROTECTION.md`](../team/BRANCH_PROTECTION.md)

### CI/CD
- GitHub Actions: `.github/workflows/`
- 배포 스크립트: `scripts/`

## 📞 문제 발생 시 연락처

- **긴급 장애**: Slack #emergency 채널
- **배포 문의**: Slack #devops 채널
- **인프라 담당자**: @devops-team
