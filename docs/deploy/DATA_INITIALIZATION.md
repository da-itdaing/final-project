# 데이터 초기화 가이드

더미 데이터를 제거하고 실제 데이터로 전환하는 방법입니다.

## 📊 현재 상황

### 더미 데이터 위치
```
itdaing-web/src/data/
├── dummyPopups.ts       # 팝업 더미 데이터
├── dummyUsers.ts        # 사용자 더미 데이터  
└── dummyCategories.ts   # 카테고리 더미 데이터
```

### 문제점
- 프론트엔드가 하드코딩된 더미 데이터 사용
- 백엔드 API와 연동되지 않음
- 실제 데이터베이스에 데이터가 없음

---

## 🎯 목표

1. ✅ 백엔드 데이터베이스에 마스터 데이터 입력
2. ✅ 프론트엔드를 백엔드 API와 연동
3. ✅ 더미 데이터 제거 또는 Mock 모드로 전환

---

## 📥 1단계: 마스터 데이터 준비

### 마스터 데이터란?
시스템에서 공통으로 사용되는 기준 데이터:
- 카테고리 (패션, 뷰티, 음식 등)
- 스타일 (감성적인, 혼자여도 좋은 등)
- 지역 (남구, 북구 등)
- 특징 (포토존, 체험형 등)

### SQL 스크립트 작성

`src/main/resources/db/migration/V2__insert_master_data.sql` 생성:

```sql
-- 카테고리 데이터
INSERT INTO category (name, type, created_at, updated_at) VALUES
('패션', 'CONSUMER', NOW(), NOW()),
('뷰티', 'CONSUMER', NOW(), NOW()),
('음식', 'CONSUMER', NOW(), NOW()),
('문화', 'CONSUMER', NOW(), NOW()),
('캐릭터', 'CONSUMER', NOW(), NOW()),
('리빙', 'CONSUMER', NOW(), NOW()),
('전시', 'POPUP', NOW(), NOW()),
('판매', 'POPUP', NOW(), NOW()),
('체험', 'POPUP', NOW(), NOW());

-- 스타일 데이터
INSERT INTO style (name, created_at, updated_at) VALUES
('혼자여도 좋은', NOW(), NOW()),
('감성적인', NOW(), NOW()),
('트렌디한', NOW(), NOW()),
('독특한', NOW(), NOW()),
('포토제닉한', NOW(), NOW()),
('힐링되는', NOW(), NOW());

-- 지역 데이터 (광주)
INSERT INTO region (name, created_at, updated_at) VALUES
('동구', NOW(), NOW()),
('서구', NOW(), NOW()),
('남구', NOW(), NOW()),
('북구', NOW(), NOW()),
('광산구', NOW(), NOW());

-- 특징 데이터
INSERT INTO feature (name, created_at, updated_at) VALUES
('포토존', NOW(), NOW()),
('체험형', NOW(), NOW()),
('굿즈', NOW(), NOW()),
('한정판', NOW(), NOW()),
('무료 입장', NOW(), NOW()),
('주차 가능', NOW(), NOW()),
('반려동물 동반', NOW(), NOW());
```

---

## 🚀 2단계: 데이터베이스 마이그레이션

### 로컬 환경

```bash
# 1. 애플리케이션 실행 (Flyway 자동 실행)
./gradlew bootRun --args='--spring.profiles.active=local'

# 2. H2 콘솔에서 확인
open http://localhost:8080/h2-console

# SQL 실행
SELECT * FROM category;
SELECT * FROM style;
SELECT * FROM region;
SELECT * FROM feature;
```

### 프로덕션 환경 (EC2)

```bash
# SSH 접속
ssh itdaing-prod

# 데이터베이스 접속
mysql -h <RDS_ENDPOINT> -u admin -p

# 데이터베이스 선택
USE itdaing;

# 마스터 데이터 입력
SOURCE /home/ubuntu/final-project/src/main/resources/db/migration/V2__insert_master_data.sql

# 확인
SELECT * FROM category;
SELECT COUNT(*) FROM category;
```

---

## 🔄 3단계: 프론트엔드 API 연동

### API 클라이언트 생성

```bash
cd itdaing-web

# OpenAPI 스펙으로 타입 생성
pnpm gen:api

# 생성된 파일 확인
ls -la src/api/
```

### 더미 데이터 사용 중단

#### Before (더미 데이터 사용)
```typescript
// src/pages/PopupList.tsx
import { dummyPopups } from '@/data/dummyPopups';

function PopupList() {
  const [popups, setPopups] = useState(dummyPopups);
  
  return (
    <div>
      {popups.map(popup => (
        <PopupCard key={popup.id} popup={popup} />
      ))}
    </div>
  );
}
```

#### After (API 연동)
```typescript
// src/pages/PopupList.tsx
import { getPopups } from '@/api';
import { useEffect, useState } from 'react';

function PopupList() {
  const [popups, setPopups] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  useEffect(() => {
    async function fetchPopups() {
      try {
        setLoading(true);
        const response = await getPopups();
        setPopups(response.data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }
    
    fetchPopups();
  }, []);
  
  if (loading) return <div>로딩 중...</div>;
  if (error) return <div>에러: {error}</div>;
  
  return (
    <div>
      {popups.map(popup => (
        <PopupCard key={popup.id} popup={popup} />
      ))}
    </div>
  );
}
```

---

## 🧹 4단계: 더미 데이터 정리

### 옵션 1: 더미 데이터 삭제 (권장)

```bash
cd itdaing-web

# 더미 데이터 파일 삭제
rm -rf src/data/dummy*.ts

# 또는 백업 후 삭제
mkdir -p archive
mv src/data/dummy*.ts archive/
```

### 옵션 2: Mock 모드 유지 (개발용)

```typescript
// src/config/apiConfig.ts
const ENABLE_MOCK = import.meta.env.VITE_ENABLE_MOCK === 'true';

export async function getPopups() {
  if (ENABLE_MOCK) {
    // 개발 모드: 더미 데이터 반환
    return { data: dummyPopups };
  }
  
  // 프로덕션: 실제 API 호출
  const response = await fetch('/api/popups');
  return response.json();
}
```

```bash
# .env.local (개발)
VITE_ENABLE_MOCK=true

# .env.production (프로덕션)
VITE_ENABLE_MOCK=false
```

---

## 📝 5단계: 초기 데이터 입력 (관리자)

### 관리자 계정 생성

```sql
-- 관리자 계정 INSERT
INSERT INTO users (login_id, email, password, name, role, created_at, updated_at) VALUES
('admin', 'admin@daitdaing.link', 
 '$2a$10$encrypted_password_here',  -- BCrypt 암호화된 비밀번호
 '관리자', 'ADMIN', NOW(), NOW());
```

### 비밀번호 암호화

```java
// PasswordEncoder를 사용하여 암호화
// 임시 컨트롤러나 테스트 코드에서 실행
@Autowired
private PasswordEncoder passwordEncoder;

String rawPassword = "admin123!@#";
String encoded = passwordEncoder.encode(rawPassword);
System.out.println(encoded);
// $2a$10$XYZ... 형태의 암호화된 문자열 출력
```

### 관리자 페이지에서 데이터 입력

```
1. 관리자 로그인
   URL: https://admin.daitdaing.link
   ID: admin
   PW: admin123!@#

2. 팝업 등록
   - 팝업 정보 입력
   - 이미지 업로드
   - 카테고리/스타일/지역 선택
   - 저장

3. 확인
   - 일반 사용자 페이지에서 확인
   - https://aischool.daitdaing.link
```

---

## 🧪 6단계: 데이터 검증

### API 테스트

```bash
# 카테고리 조회
curl https://aischool.daitdaing.link/api/master/categories

# 스타일 조회
curl https://aischool.daitdaing.link/api/master/styles

# 지역 조회
curl https://aischool.daitdaing.link/api/master/regions

# 팝업 목록 조회
curl https://aischool.daitdaing.link/api/popups
```

### 프론트엔드에서 확인

```
1. 브라우저 열기
   https://aischool.daitdaing.link

2. 개발자 도구 (F12)
   - Network 탭 확인
   - API 호출 확인
   - 응답 데이터 확인

3. 페이지 동작 확인
   - 팝업 목록 표시
   - 필터링 동작
   - 상세 페이지 이동
```

---

## 📊 7단계: 대량 데이터 입력

### CSV 파일 준비

```csv
# popups.csv
title,description,location,start_date,end_date,category_id,status
"봄 팝업 스토어","봄 신상품 전시","광주 남구 봉선동",2025-03-01,2025-03-31,1,APPROVED
"여름 체험 팝업","시원한 여름 체험","광주 북구 용봉동",2025-06-01,2025-08-31,3,PENDING
```

### Python 스크립트로 대량 입력

```python
# scripts/import_popups.py
import csv
import requests

API_BASE_URL = "https://aischool.daitdaing.link/api"
ADMIN_TOKEN = "your_admin_jwt_token"

headers = {
    "Authorization": f"Bearer {ADMIN_TOKEN}",
    "Content-Type": "application/json"
}

with open('popups.csv', 'r', encoding='utf-8') as f:
    reader = csv.DictReader(f)
    for row in reader:
        payload = {
            "title": row['title'],
            "description": row['description'],
            "location": row['location'],
            "startDate": row['start_date'],
            "endDate": row['end_date'],
            "categoryId": int(row['category_id']),
            "status": row['status']
        }
        
        response = requests.post(
            f"{API_BASE_URL}/popups",
            json=payload,
            headers=headers
        )
        
        if response.status_code == 201:
            print(f"✅ {row['title']} 등록 성공")
        else:
            print(f"❌ {row['title']} 실패: {response.text}")
```

```bash
# 실행
python scripts/import_popups.py
```

---

## 🔄 8단계: 데이터 백업

### 데이터베이스 백업

```bash
# 전체 데이터베이스 덤프
mysqldump -h <RDS_ENDPOINT> -u admin -p itdaing > backup_$(date +%Y%m%d).sql

# 특정 테이블만
mysqldump -h <RDS_ENDPOINT> -u admin -p itdaing \
  category style region feature > master_data_backup.sql
```

### 정기 백업 설정 (Cron)

```bash
# crontab 편집
crontab -e

# 매일 새벽 3시 백업
0 3 * * * /home/ubuntu/scripts/backup_db.sh

# backup_db.sh
#!/bin/bash
BACKUP_DIR=/home/ubuntu/backups
mkdir -p $BACKUP_DIR

mysqldump -h <RDS_ENDPOINT> -u admin -p"$DB_PASSWORD" itdaing \
  > $BACKUP_DIR/itdaing_$(date +\%Y\%m\%d_\%H\%M\%S).sql

# 7일 이상 된 백업 삭제
find $BACKUP_DIR -name "*.sql" -mtime +7 -delete
```

---

## 🐛 문제 해결

### 마스터 데이터가 조회되지 않음

```bash
# 1. 데이터 확인
mysql -h <RDS_ENDPOINT> -u admin -p
USE itdaing;
SELECT COUNT(*) FROM category;

# 2. 데이터가 없으면 수동 입력
SOURCE /path/to/V2__insert_master_data.sql;

# 3. 애플리케이션 재시작
cd /home/ubuntu/app
./deploy.sh
```

### 프론트엔드에서 API 호출 실패

```typescript
// 1. 환경 변수 확인
console.log(import.meta.env.VITE_API_BASE_URL);

// 2. Network 탭에서 에러 확인
// - 404: 엔드포인트 경로 확인
// - 401: 인증 토큰 확인
// - 500: 서버 로그 확인

// 3. CORS 에러 시
// 백엔드 application.yml 확인
cors:
  allowed-origins: https://aischool.daitdaing.link,https://admin.daitdaing.link
```

---

## ✅ 체크리스트

### 백엔드 데이터 준비
- [ ] 마스터 데이터 SQL 작성
- [ ] Flyway 마이그레이션 실행
- [ ] 데이터베이스에 데이터 확인
- [ ] API 엔드포인트 테스트

### 프론트엔드 연동
- [ ] OpenAPI 스펙으로 타입 생성
- [ ] 더미 데이터 제거 또는 Mock 모드 설정
- [ ] API 호출 로직 구현
- [ ] 로딩/에러 처리 추가
- [ ] 환경 변수 설정

### 관리자 기능
- [ ] 관리자 계정 생성
- [ ] 관리자 페이지 접속 확인
- [ ] 팝업 등록 기능 테스트
- [ ] 이미지 업로드 테스트

### 운영 준비
- [ ] 데이터 백업 스크립트 작성
- [ ] 정기 백업 Cron 설정
- [ ] 모니터링 설정
- [ ] 롤백 계획 수립

---

## 📚 참고 자료

- [Flyway Documentation](https://flywaydb.org/documentation/)
- [MySQL Dump/Restore](https://dev.mysql.com/doc/refman/8.0/en/mysqldump.html)
- [OpenAPI Generator](https://openapi-generator.tech/)
