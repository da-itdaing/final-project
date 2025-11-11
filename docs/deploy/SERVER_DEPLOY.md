# EC2 서버 배포 가이드 (SSH 접속 환경)

EC2 Ubuntu 서버에 SSH로 접속하여 배포하는 방법입니다.

## 🖥️ 서버 정보

### 프로덕션 환경
- **서버**: AWS EC2 (Ubuntu)
- **도메인**: https://aischool.daitdaing.link
- **관리자**: https://admin.daitdaing.link
- **포트**: 8080 (백엔드), 80/443 (Nginx)

---

## 🔐 SSH 접속

### 1. SSH 키 설정

```bash
# 로컬에서 실행
# SSH 키 권한 설정
chmod 400 ~/.ssh/id_rsa

# SSH Config 설정 (~/.ssh/config)
Host bastion
  HostName -
  User ubuntu
  IdentityFile ~/.ssh/id_rsa
  IdentitiesOnly yes
  ForwardAgent yes

Host private-ec2
  HostName -
  User ubuntu
  ProxyJump bastion
  IdentityFile ~/.ssh/id_rsa
  IdentitiesOnly yes
  ServerAliveInterval 60
  ConnectTimeout 30
  ForwardAgent yes

# Bastion을 통해 Private EC2 접속
ssh private-ec2
```

**접속 구조:**
```
로컬 PC → Bastion (3.38.99.166) → Private EC2 (10.0.133.168)
```

**주의사항:**
- Bastion IP와 Private EC2 IP는 변경될 수 있습니다
- IP 변경 시 `~/.ssh/config` 파일의 `HostName`만 수정하면 됩니다
- `ProxyJump`를 통해 Bastion을 경유하여 Private EC2에 접속합니다

### 2. 첫 접속 시 환경 설정

```bash
# 시스템 업데이트
sudo apt update && sudo apt upgrade -y

# Java 21 설치 확인
java -version

# 없으면 설치
sudo apt install openjdk-21-jdk -y

# Git 설치 확인
git --version

# 없으면 설치
sudo apt install git -y
```

---

## 📁 서버 디렉토리 구조

```
/home/ubuntu/
├── app/                          # 애플리케이션 디렉토리
│   ├── itdaing-0.0.1-SNAPSHOT.jar  # 실행 중인 JAR
│   ├── deploy.sh                 # 배포 스크립트
│   ├── backup/                   # 이전 버전 백업
│   └── logs/                     # 애플리케이션 로그
│
├── final-project/                # Git 저장소 (선택사항)
│   └── (프로젝트 파일들)
│
└── .env                          # 환경 변수
```

---

## 🚀 배포 방법

### 방법 1: Git Pull 배포 (권장)

#### 1단계: 저장소 클론 (최초 1회)

```bash
# Bastion을 통해 Private EC2 접속
ssh private-ec2

# 프로젝트 디렉토리로 이동
cd /home/ubuntu

# 저장소 클론 (최초 1회만)
git clone https://github.com/da-itdaing/final-project.git
cd final-project

# main 브랜치로 이동
git checkout main
```

#### 2단계: 코드 업데이트

```bash
# Private EC2에서 실행
cd /home/ubuntu/final-project

# 최신 코드 받기
git fetch origin main
git pull origin main

# 변경사항 확인
git log -3 --oneline
```

#### 3단계: 빌드

```bash
# Gradle로 빌드
./gradlew clean bootJar

# 빌드 결과 확인
ls -lh build/libs/
```

#### 4단계: 배포

```bash
# 기존 JAR 백업
cp /home/ubuntu/app/itdaing-0.0.1-SNAPSHOT.jar \
   /home/ubuntu/app/backup/itdaing-$(date +%Y%m%d_%H%M%S).jar

# 새 JAR 복사
cp build/libs/itdaing-0.0.1-SNAPSHOT.jar /home/ubuntu/app/

# 애플리케이션 재시작
cd /home/ubuntu/app
./deploy.sh
```

### 방법 2: 로컬 빌드 → SCP 전송

#### 로컬에서 실행

```bash
# 1. 로컬에서 빌드
./gradlew clean bootJar

# 2. Bastion을 경유하여 Private EC2로 전송
# ProxyJump를 사용하므로 간단히 전송 가능
scp build/libs/itdaing-0.0.1-SNAPSHOT.jar \
    private-ec2:/home/ubuntu/app/

# 또는 Bastion을 거쳐 명시적으로 전송
scp -o ProxyJump=bastion \
    build/libs/itdaing-0.0.1-SNAPSHOT.jar \
    ubuntu@10.0.133.168:/home/ubuntu/app/
```

#### 서버에서 실행

```bash
# Private EC2 접속
ssh private-ec2

# 재시작
cd /home/ubuntu/app
./deploy.sh
```

---

## 📝 배포 스크립트

### deploy.sh 작성

```bash
# /home/ubuntu/app/deploy.sh
#!/bin/bash

APP_NAME=itdaing
JAR_FILE=/home/ubuntu/app/itdaing-0.0.1-SNAPSHOT.jar
LOG_DIR=/home/ubuntu/app/logs
PID_FILE=/home/ubuntu/app/app.pid

# 로그 디렉토리 생성
mkdir -p $LOG_DIR

# 기존 프로세스 종료
if [ -f $PID_FILE ]; then
    OLD_PID=$(cat $PID_FILE)
    if ps -p $OLD_PID > /dev/null; then
        echo "Stopping old process (PID: $OLD_PID)..."
        kill $OLD_PID
        sleep 5
        
        # 강제 종료 (필요시)
        if ps -p $OLD_PID > /dev/null; then
            echo "Force killing..."
            kill -9 $OLD_PID
        fi
    fi
fi

# 환경 변수 로드
if [ -f /home/ubuntu/.env ]; then
    source /home/ubuntu/.env
fi

# 새 프로세스 시작
echo "Starting new process..."
nohup java -jar \
    -Dspring.profiles.active=prod \
    -Dserver.port=8080 \
    $JAR_FILE \
    > $LOG_DIR/application.log 2>&1 &

# PID 저장
NEW_PID=$!
echo $NEW_PID > $PID_FILE

echo "Application started with PID: $NEW_PID"
echo "Logs: $LOG_DIR/application.log"

# 시작 확인 (10초 대기)
sleep 10
if ps -p $NEW_PID > /dev/null; then
    echo "✅ Application is running"
    
    # 헬스 체크
    curl -s http://localhost:8080/actuator/health
else
    echo "❌ Application failed to start"
    echo "Check logs: tail -f $LOG_DIR/application.log"
    exit 1
fi
```

### 스크립트 권한 설정

```bash
chmod +x /home/ubuntu/app/deploy.sh
```

---

## 🔧 환경 변수 설정

### /home/ubuntu/.env 파일 생성

```bash
# 편집
nano /home/ubuntu/.env

# 또는
vim /home/ubuntu/.env
```

### 환경 변수 내용

```bash
# 데이터베이스
export DB_URL=jdbc:mysql://your-rds-endpoint:3306/itdaing
export DB_USERNAME=admin
export DB_PASSWORD=your_secure_password

# JWT
export JWT_SECRET=your_production_jwt_secret_key_change_this

# AWS (S3 등)
export AWS_ACCESS_KEY=your_aws_access_key
export AWS_SECRET_KEY=your_aws_secret_key
export AWS_REGION=ap-northeast-2

# 도메인
export ALLOWED_ORIGINS=https://aischool.daitdaing.link,https://admin.daitdaing.link

# CORS
export CORS_ALLOWED_ORIGINS=https://aischool.daitdaing.link,https://admin.daitdaing.link
```

### 환경 변수 적용

```bash
# 현재 세션에 적용
source /home/ubuntu/.env

# 확인
echo $DB_URL
```

---

## 🔍 모니터링 및 로그

### 애플리케이션 상태 확인

```bash
# 프로세스 확인
ps aux | grep java

# PID로 확인
cat /home/ubuntu/app/app.pid
ps -p $(cat /home/ubuntu/app/app.pid)

# 포트 확인
sudo netstat -tulpn | grep 8080
```

### 로그 확인

```bash
# 실시간 로그
tail -f /home/ubuntu/app/logs/application.log

# 최근 100줄
tail -n 100 /home/ubuntu/app/logs/application.log

# 에러 로그만
grep ERROR /home/ubuntu/app/logs/application.log

# 특정 시간대 로그
grep "2025-11-11 14:" /home/ubuntu/app/logs/application.log
```

### 헬스 체크

```bash
# 로컬 헬스 체크
curl http://localhost:8080/actuator/health

# 외부 접근 (Nginx 통과)
curl https://aischool.daitdaing.link/actuator/health
```

---

## 🔄 Git 관리 (서버 내)

### 브랜치 관리

```bash
# Private EC2에서 실행
ssh private-ec2

cd /home/ubuntu/final-project

# 현재 브랜치 확인
git branch

# main 브랜치로 전환
git checkout main

# 최신 상태 확인
git status
git log -3 --oneline
```

### 충돌 해결

```bash
# Private EC2에서 Pull 시 충돌 발생
git pull origin main
# CONFLICT...

# 충돌 파일 확인
git status

# 로컬 변경사항 버리고 원격 받기 (주의!)
git fetch origin main
git reset --hard origin/main

# 또는 stash 사용
git stash
git pull origin main
git stash pop
```

### 태그 배포

```bash
# Private EC2에서 특정 태그로 배포
git fetch --tags
git checkout tags/v1.2.0

# 빌드 및 배포
./gradlew clean bootJar
cp build/libs/itdaing-0.0.1-SNAPSHOT.jar /home/ubuntu/app/
cd /home/ubuntu/app
./deploy.sh
```

---

## 🐛 문제 해결

### 애플리케이션이 시작되지 않을 때

```bash
# 1. 로그 확인
tail -f /home/ubuntu/app/logs/application.log

# 2. Java 프로세스 확인
ps aux | grep java

# 3. 포트 충돌 확인
sudo lsof -i :8080

# 4. 메모리 확인
free -h

# 5. 디스크 확인
df -h
```

### 포트 충돌 해결

```bash
# 8080 포트 사용 프로세스 찾기
sudo lsof -i :8080

# 프로세스 강제 종료
sudo kill -9 <PID>

# 또는 deploy.sh 실행 (자동 종료)
./deploy.sh
```

### Out of Memory 에러

```bash
# JVM 힙 메모리 조정
# deploy.sh 수정
nohup java -jar \
    -Xms512m \
    -Xmx1024m \
    -Dspring.profiles.active=prod \
    $JAR_FILE \
    > $LOG_DIR/application.log 2>&1 &
```

---

## 🔄 롤백 방법

### 이전 버전으로 복구

```bash
# Private EC2 접속
ssh private-ec2

cd /home/ubuntu/app

# 1. 백업 목록 확인
ls -lht backup/

# 2. 이전 버전 복사
cp backup/itdaing-20251111_140000.jar itdaing-0.0.1-SNAPSHOT.jar

# 3. 재시작
./deploy.sh
```

### Git 커밋 롤백

```bash
# Private EC2에서 실행
ssh private-ec2

cd /home/ubuntu/final-project

# 1. 이전 커밋 확인
git log --oneline -10

# 2. 특정 커밋으로 이동
git checkout <commit-hash>

# 3. 빌드 및 배포
./gradlew clean bootJar
cp build/libs/itdaing-0.0.1-SNAPSHOT.jar /home/ubuntu/app/
cd /home/ubuntu/app
./deploy.sh
```

---

## 📊 시스템 리소스 모니터링

### CPU/메모리 사용량

```bash
# Private EC2에서 실행
ssh private-ec2

# 실시간 모니터링
top

# Java 프로세스만
top -p $(cat /home/ubuntu/app/app.pid)

# htop (설치 필요)
sudo apt install htop -y
htop
```

### 디스크 사용량

```bash
# Private EC2에서 실행
# 전체 디스크
df -h

# 특정 디렉토리
du -sh /home/ubuntu/app/*
du -sh /home/ubuntu/final-project/*
```

---

## 🔐 보안 체크리스트

### Private EC2 보안

- [ ] Bastion을 통한 간접 접속 (Public IP 없음)
- [ ] SSH 키 기반 인증 사용 (`IdentitiesOnly yes`)
- [ ] ForwardAgent로 Git 인증 전달
- [ ] Security Group으로 Bastion만 SSH 허용
- [ ] Private Subnet에 EC2 배치
- [ ] 정기적인 시스템 업데이트
- [ ] 로그 로테이션 설정
- [ ] 데이터베이스 백업 자동화

### Bastion 보안

- [ ] SSH 키 기반 인증만 허용 (비밀번호 인증 비활성화)
- [ ] fail2ban 설치 (brute force 방어)
- [ ] 특정 IP만 SSH 접속 허용 (Security Group)
- [ ] 불필요한 포트 모두 닫기
- [ ] 정기적인 보안 업데이트

### Private EC2 방화벽 설정

```bash
# Private EC2에서 실행
ssh private-ec2

# UFW 활성화
sudo ufw enable

# Bastion에서 SSH 허용
sudo ufw allow from 10.0.0.0/16 to any port 22

# HTTP/HTTPS 허용 (Nginx)
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp

# 상태 확인
sudo ufw status
```

---

## 📝 배포 체크리스트

### 배포 전
- [ ] 로컬에서 테스트 통과 확인
- [ ] dev/integration 환경에서 정상 동작 확인
- [ ] 데이터베이스 마이그레이션 준비
- [ ] 환경 변수 설정 확인
- [ ] 백업 생성
- [ ] Bastion 접속 가능 여부 확인

### 배포 중
- [ ] Bastion → Private EC2 접속 (`ssh private-ec2`)
- [ ] Git pull 또는 JAR 전송 (SCP with ProxyJump)
- [ ] 빌드 성공 확인
- [ ] 기존 버전 백업
- [ ] 새 버전 배포
- [ ] 헬스 체크

### 배포 후
- [ ] 애플리케이션 정상 시작 확인
- [ ] API 엔드포인트 동작 확인
- [ ] 로그 모니터링 (최소 10분)
- [ ] 관리자 페이지 접속 확인
- [ ] 팀 Slack에 배포 완료 공지

---

## 🔗 관련 문서

- [로컬 배포 가이드](./LOCAL_DEPLOY.md)
- [프로덕션 배포 자동화](./PRODUCTION_DEPLOY.md)
- [데이터 초기화 가이드](./DATA_INITIALIZATION.md)
- [Nginx 설정](./NGINX_SETUP.md)
