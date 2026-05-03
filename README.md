# SYNK Backend

> "우리는 지금, 같은 순간을 산다"

친구들과 랜덤 시간에 동시 미션을 수행하는 실시간 동기화 소셜 서비스

## 🛠️ 기술 스택

- **Java** 17
- **Spring Boot** 3.2.x
- **Spring Data JPA** + Hibernate
- **Spring Security** + JWT
- **MySQL** 8.0
- **Gradle**
- **AWS S3** (이미지 저장)
- **Firebase Cloud Messaging** (푸시 알림)

## 🚀 로컬 실행 방법

### 1. MySQL 데이터베이스 생성
```sql
CREATE DATABASE synk CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. application.yml 설정
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/synk
    username: root
    password: [your-password]
```

### 3. 프로젝트 실행
```bash
./gradlew bootRun
```

서버 실행: http://localhost:8080

## 📊 ERD

주요 테이블:
- `users` - 회원 정보
- `rooms` - 방 정보
- `room_members` - 유저-방 N:M 관계
- `missions` - 미션 정보
- `submissions` - 제출물
- `collages` - 그룹 콜라주
- `mission_chats` - 채팅

자세한 내용은 [CLAUDE.md](./CLAUDE.md) 참고

## 📁 프로젝트 구조
src/
├── main/
│   ├── java/com/synk/backend/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── entity/
│   │   ├── dto/
│   │   ├── config/
│   │   └── BackendApplication.java
│   └── resources/
│       └── application.yml
└── test/

## 🎯 개발 로드맵

- [x] Spring Boot 프로젝트 세팅
- [ ] 회원가입 & 로그인
- [ ] 방 생성 & 참여
- [ ] 미션 발동
- [ ] 사진 제출
- [ ] 콜라주 생성
- [ ] 60초 라이브 톡
- [ ] 패스권 시스템
- [ ] 랜덤 사이렌
- [ ] FCM 푸시 알림

## 📝 Git 커밋 컨벤션
feat: 새 기능 추가
fix: 버그 수정
docs: 문서 수정
refactor: 리팩토링
test: 테스트 추가
chore: 빌드/설정 변경

## 👨‍💻 개발자

- **GitHub**: https://github.com/youhyun010615

## 📄 License

MIT License