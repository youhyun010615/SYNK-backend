# SYNK 백엔드 프로젝트

## 프로젝트 소개
"우리는 지금, 같은 순간을 산다"
친구들과 랜덤 시간에 동시 미션을 수행하는 실시간 동기화 소셜 서비스

## 기술 스택
- **Backend**: Spring Boot 3.2.x + Java 17
- **Database**: MySQL + Spring Data JPA
- **Auth**: Spring Security + JWT
- **Build**: Gradle

---

## 핵심 기능

### 1. 랜덤 사이렌
- 예측 불가능한 시간에 그룹 전원에게 동시 알림
- FCM 푸시 알림

### 2. 30초 타임어택
- 알림 후 30초 내 미션 수행 (사진/영상/소리)
- 듀얼 샷: 전면 + 후면 카메라 동시 촬영
- 메타데이터: 위치(GPS), 소음도(dB)

### 3. 그룹 콜라주
- 제출물을 하나의 이미지로 합성
- 동기화 점수 계산 (제출 시간 오차)

### 4. 60초 라이브 톡
- 콜라주 공유 후 60초간 실시간 채팅

### 5. 패널티 시스템
- 블랙아웃: 미참여자는 콜라주 볼 수 없음
- 패스권: 월 3회 제한

---

## ERD (데이터베이스 설계)

### 테이블 목록

#### 1. users (회원)
- id (PK, BIGINT, AUTO_INCREMENT)
- user_id (VARCHAR(50), UNIQUE, NOT NULL) - 로그인 ID
- user_password (VARCHAR(255), NOT NULL) - 암호화된 비밀번호
- name (VARCHAR(100), NOT NULL)
- profile_image (VARCHAR(255))
- pass_count (INT, DEFAULT 0) - 이번 달 패스권 사용 횟수
- pass_reset_at (DATE) - 패스권 마지막 리셋 날짜
- fcm_token (VARCHAR(255)) - 푸시 알림 토큰
- created_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)

#### 2. rooms (방)
- id (PK, BIGINT, AUTO_INCREMENT)
- code (VARCHAR(10), UNIQUE, NOT NULL) - 초대 코드 (예: ABC123)
- max_members (INT, DEFAULT 10) - 최대 인원수
- created_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)

#### 3. room_members (방 멤버 - User와 Room의 N:M 관계)
- id (PK, BIGINT, AUTO_INCREMENT)
- user_id (FK → users.id, NOT NULL)
- room_id (FK → rooms.id, NOT NULL)
- join_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)
- 제약: UNIQUE(user_id, room_id) - 중복 가입 방지

#### 4. missions (미션)
- id (PK, BIGINT, AUTO_INCREMENT)
- room_id (FK → rooms.id, NOT NULL)
- mission_type (ENUM: PHOTO, VIDEO, SOUND, NOT NULL)
- mission_status (ENUM: PENDING, ACTIVE, COMPLETED, EXPIRED, DEFAULT PENDING)
- targeted_at (TIMESTAMP, NOT NULL) - 미션 발동 시간
- deadline (TIMESTAMP, NOT NULL) - 마감 시간 (발동 + 30초)
- created_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)

#### 5. submissions (제출물)
- id (PK, BIGINT, AUTO_INCREMENT)
- user_id (FK → users.id, NOT NULL)
- mission_id (FK → missions.id, NOT NULL)
- front_camera_url (VARCHAR(255)) - 전면 카메라 사진 URL
- back_camera_url (VARCHAR(255)) - 후면 카메라 사진 URL
- video_url (VARCHAR(255))
- sound_url (VARCHAR(255))
- location (JSON) - 위치 정보 {"lat": 37.5, "lng": 127.0}
- noise_level (INT) - 소음도 (dB)
- status (ENUM: SUBMITTED, MISSED, PASSED, DEFAULT SUBMITTED)
- submitted_at (TIMESTAMP) - 실제 제출 시간 (DEFAULT 없음)
- created_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)
- 제약: UNIQUE(mission_id, user_id) - 중복 제출 방지

#### 6. collages (콜라주 - Mission과 1:1 관계)
- id (PK, BIGINT, AUTO_INCREMENT)
- mission_id (FK → missions.id, UNIQUE, NOT NULL)
- collage_file_url (VARCHAR(255), NOT NULL) - 합성된 이미지 URL
- collage_status (ENUM: COMPLETED, EXPIRED, DEFAULT COMPLETED)
- sync_score (INT) - 동기화 점수 (0~100)
- created_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)

#### 7. mission_chats (채팅)
- id (PK, BIGINT, AUTO_INCREMENT)
- mission_id (FK → missions.id, NOT NULL)
- user_id (FK → users.id, NOT NULL)
- message_type (ENUM: TEXT, EMOJI, NOT NULL)
- content (TEXT, NOT NULL)
- created_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)

### 관계도
```
users (1) ----< (N) room_members (N) >---- (1) rooms
|                                            |
|                                            |
v                                            v
submissions (N) >---- (1) missions (1) ---- collages
|
|
v
mission_chats (N)
```

---

## 개발 순서

### Phase 1: 인증 (현재 진행)
- [ ] User Entity 생성
- [ ] Spring Security + JWT 설정
- [ ] 회원가입 API (POST /api/auth/signup)
- [ ] 로그인 API (POST /api/auth/login)

### Phase 2: 방 기능
- [ ] Room, RoomMember Entity 생성
- [ ] 방 생성 API (POST /api/rooms) - 랜덤 코드 생성
- [ ] 방 참여 API (POST /api/rooms/join) - 코드 입력
- [ ] 내 방 목록 조회 (GET /api/rooms/my)
- [ ] 방 멤버 조회 (GET /api/rooms/{id}/members)

### Phase 3: 미션 (수동 발동)
- [ ] Mission Entity 생성
- [ ] 미션 생성 API (POST /api/missions) - 테스트용
- [ ] 진행 중인 미션 조회 (GET /api/missions/active)

### Phase 4: 제출
- [ ] Submission Entity 생성
- [ ] 이미지 업로드 (Multipart + S3)
- [ ] 제출 API (POST /api/submissions)
- [ ] 제출 현황 조회 (GET /api/missions/{id}/submissions)

### Phase 5: 콜라주
- [ ] Collage Entity 생성
- [ ] 이미지 합성 로직
- [ ] 동기화 점수 계산
- [ ] 콜라주 조회 API (GET /api/missions/{id}/collage)

### Phase 6: 채팅
- [ ] MissionChat Entity 생성
- [ ] 채팅 전송 API (POST /api/chats)
- [ ] 채팅 조회 API (GET /api/missions/{id}/chats)

### Phase 7: 패스권
- [ ] 패스권 사용 API (POST /api/submissions/pass)
- [ ] 월별 리셋 로직

### Phase 8: 자동화
- [ ] @Scheduled 랜덤 사이렌
- [ ] FCM 푸시 알림

---

## 중요 규칙

### ENUM 사용 필수
모든 상태 컬럼은 ENUM 타입 (VARCHAR 사용 시 오타 발생 가능)

### UNIQUE 제약
- room_members: UNIQUE(user_id, room_id)
- submissions: UNIQUE(mission_id, user_id)

### 시간 처리
- submissions.submitted_at: DEFAULT 없음 (실제 제출 시간)
- 나머지 created_at: DEFAULT CURRENT_TIMESTAMP

### 데이터 정합성
- Collage의 참여자 목록은 별도 저장 안 함
- Submission 조회로 계산

---

## API 응답 형식

### 성공
```json
{
  "success": true,
  "data": { ... },
  "message": "성공 메시지"
}
```

### 실패
```json
{
  "success": false,
  "error": "에러 메시지",
  "code": "ERROR_CODE"
}
```

---

## 개발 시 참고사항

### 패키지 구조
```
com.synk.backend.controller
com.synk.backend.service
com.synk.backend.repository
com.synk.backend.entity
com.synk.backend.dto.request
com.synk.backend.dto.response
com.synk.backend.config
com.synk.backend.exception
com.synk.backend.util
```

### 네이밍 규칙
- Entity: User, Room, Mission
- DTO: UserSignupRequest, UserResponse
- Service: UserService, RoomService
- Controller: UserController, RoomController

### application.yml 기본 설정
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/synk
    username: root
    password: 
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```