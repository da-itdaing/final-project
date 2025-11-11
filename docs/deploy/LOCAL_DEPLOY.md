# 로컬 개발 환경 배포 가이드

로컬 개발 환경에서 프론트엔드와 백엔드를 실행하는 방법입니다.

## 🖥️ 로컬 환경 구성

### 백엔드 (Spring Boot)
```
포트: 8080
URL: http://localhost:8080
API Docs: http://localhost:8080/swagger-ui.html
```

### 프론트엔드 (Vite)
```
포트: 5173
URL: http://localhost:5173
```

---

## 🚀 백엔드 로컬 실행

### 1. 환경 변수 설정

`src/main/resources/application-local.yml` 생성:
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  
  h2:
    console:
      enabled: true
      path: /h2-console
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  
  jwt:
    secret: local-development-secret-key-change-in-production
    access-token-expiration: 3600000
    refresh-token-expiration: 1209600000

server:
  port: 8080

logging:
  level:
    com.da.itdaing: DEBUG
```

### 2. 실행 명령어

#### IntelliJ IDEA
```
1. Run Configuration 생성
   - Main class: com.da.itdaing.ItdaingApplication
   - Active profiles: local
   - JVM options: -Dspring.profiles.active=local

2. Run 버튼 클릭
```

#### Gradle 명령어
```bash
# 프로젝트 루트에서
./gradlew bootRun --args='--spring.profiles.active=local'

# 또는
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

#### JAR 빌드 후 실행
```bash
# 1. 빌드
./gradlew clean bootJar

# 2. 실행
java -jar -Dspring.profiles.active=local build/libs/itdaing-0.0.1-SNAPSHOT.jar
```

### 3. 실행 확인

```bash
# 헬스 체크
curl http://localhost:8080/actuator/health

# API 문서 접속
open http://localhost:8080/swagger-ui.html

# H2 콘솔 접속 (데이터 확인)
open http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:testdb
```

---

## 🎨 프론트엔드 로컬 실행

### 1. 환경 변수 설정

`itdaing-web/.env.local` 생성:
```bash
# API 엔드포인트
VITE_API_BASE_URL=http://localhost:8080

# 환경
VITE_APP_ENV=local

# 기타 설정
VITE_ENABLE_MOCK=false
```

### 2. 실행 명령어

```bash
cd itdaing-web

# 의존성 설치 (최초 1회)
pnpm install

# 개발 서버 실행
pnpm dev

# 브라우저 자동 오픈
# http://localhost:5173
```

### 3. Hot Reload

파일 저장 시 자동으로 브라우저가 리로드됩니다.
- React 컴포넌트 수정 → 즉시 반영
- CSS 수정 → 즉시 반영
- 설정 파일 수정 → 서버 재시작 필요

---

## 🔄 전체 스택 동시 실행

### VS Code Tasks (추천)

`.vscode/tasks.json` 생성:
```json
{
  "version": "2.0.0",
  "tasks": [
    {
      "label": "Run Backend",
      "type": "shell",
      "command": "./gradlew bootRun --args='--spring.profiles.active=local'",
      "isBackground": true,
      "problemMatcher": []
    },
    {
      "label": "Run Frontend",
      "type": "shell",
      "command": "cd itdaing-web && pnpm dev",
      "isBackground": true,
      "problemMatcher": []
    },
    {
      "label": "Run Full Stack",
      "dependsOn": ["Run Backend", "Run Frontend"],
      "problemMatcher": []
    }
  ]
}
```

실행: `Cmd+Shift+P` → `Tasks: Run Task` → `Run Full Stack`

### 터미널 탭 분할

```bash
# 터미널 1 (백엔드)
./gradlew bootRun --args='--spring.profiles.active=local'

# 터미널 2 (프론트엔드)
cd itdaing-web && pnpm dev
```

---

## 🧪 로컬 테스트 실행

### 백엔드 테스트
```bash
# 전체 테스트
./gradlew test

# 특정 테스트 클래스
./gradlew test --tests "AuthControllerTest"

# 특정 테스트 메서드
./gradlew test --tests "AuthControllerTest.signupConsumer_Success"

# 테스트 리포트
open build/reports/tests/test/index.html
```

### 프론트엔드 테스트
```bash
cd itdaing-web

# 테스트 실행
pnpm test

# Watch 모드
pnpm test:watch

# 커버리지
pnpm test:coverage
```

---

## 🐛 로컬 디버깅

### 백엔드 디버그 (IntelliJ)

1. **중단점(Breakpoint) 설정**
   - 코드 라인 번호 옆 클릭

2. **디버그 모드 실행**
   - Run → Debug 'ItdaingApplication'
   - 또는 `Ctrl+D`

3. **변수 검사**
   - Variables 탭에서 현재 변수 값 확인
   - Watches에 표현식 추가

### 프론트엔드 디버그 (Chrome DevTools)

```bash
# 개발 서버 실행
pnpm dev

# Chrome DevTools 열기
# F12 또는 우클릭 → 검사

# Sources 탭에서 중단점 설정
# Console에서 변수 확인
```

---

## 📊 데이터베이스 초기화

### H2 In-Memory (로컬 개발용)

```yaml
# application-local.yml
spring:
  jpa:
    hibernate:
      ddl-auto: create-drop  # 서버 시작 시 테이블 재생성
```

### MySQL 로컬 인스턴스 (실 DB 테스트용)

```bash
# Docker로 MySQL 실행
docker run -d \
  --name itdaing-mysql \
  -e MYSQL_ROOT_PASSWORD=rootpassword \
  -e MYSQL_DATABASE=itdaing \
  -e MYSQL_USER=itdaing_user \
  -e MYSQL_PASSWORD=itdaing_pass \
  -p 3306:3306 \
  mysql:8.0

# application-local.yml 수정
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/itdaing
    username: itdaing_user
    password: itdaing_pass
```

### Flyway 마이그레이션 실행

```bash
# 마이그레이션 스크립트 위치
# src/main/resources/db/migration/

# 서버 시작 시 자동 실행됨
./gradlew bootRun
```

---

## 🔧 문제 해결

### 포트 충돌

```bash
# 8080 포트 사용 중인 프로세스 찾기
lsof -i :8080

# 프로세스 종료
kill -9 <PID>

# 또는 다른 포트 사용
./gradlew bootRun --args='--server.port=8081'
```

### Gradle 데몬 문제

```bash
# Gradle 데몬 중지
./gradlew --stop

# 캐시 삭제
rm -rf ~/.gradle/caches/

# 재실행
./gradlew clean bootRun
```

### pnpm 의존성 문제

```bash
cd itdaing-web

# node_modules 삭제
rm -rf node_modules pnpm-lock.yaml

# 재설치
pnpm install

# 캐시 클리어
pnpm store prune
```

---

## 🔗 유용한 로컬 URL

| 서비스 | URL | 용도 |
|--------|-----|------|
| 프론트엔드 | http://localhost:5173 | 개발 서버 |
| 백엔드 API | http://localhost:8080 | REST API |
| Swagger UI | http://localhost:8080/swagger-ui.html | API 문서 |
| H2 Console | http://localhost:8080/h2-console | DB 관리 |
| Actuator Health | http://localhost:8080/actuator/health | 헬스 체크 |

---

## 📝 다음 단계

로컬 개발이 완료되면:
1. [EC2 서버 배포](./SERVER_DEPLOY.md) 참고
2. [프로덕션 배포](./PRODUCTION_DEPLOY.md) 참고
3. [데이터 초기화 가이드](./DATA_INITIALIZATION.md) 참고
