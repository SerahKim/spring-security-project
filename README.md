# 🛡️ Spring Security 기반 사용자 인증 서비스
Spring Boot와 Spring Security, JWT를 활용한 사용자 인증 시스템입니다.
이 프로젝트는 로그인, 회원가입, 토큰 재발급, 내 정보 조회 등 사용자 인증 관련 기능을 포함합니다.
___

## 📌 1. 프로젝트 개요
- 회원가입 기능
- 로그인 기능
- JWT 기반 Access Token, Refresh Token 발급 및 재발급
- Spring Security Context를 활용한 인증된 사용자 정보 조회
- 비밀번호 해싱 및 유효성 검증
- Refresh Token DB 관리

## 🔧 2. 실행 방법
### 1. 프로젝트 클론
```
git clone https://github.com/SerahKim/spring-security-project.git
```
### 2. 의존성 설치
```
./gradlew build

또는 build.gradle에서 코끼리 버튼을 눌러 의존성 설치
```
### 3. DB 설정
1. root 계정 접속 후 DB 및 개정 생성
```
mysql -u root -p
```
```
SOURCE /security/src/main/resources/db/setup.sql;
```
2. hashsnap 계정으로 접속 후 테이블 생성
```
mysql -u hashsnap -p hashsnap_db
```
```
SOURCE /security/src/main/resources/db/schema.sql;
```
### 4. 서버 실행
```
SecurityApplication 실행
```

## 🛠️ 3. 사용 기술 스택
| 구분 | 기술                          |
|------|-----------------------------|
| Language | Java 17                     |
| Framework | Spring Boot 3.5.4           |
| Security | Spring Security             |
| JWT | jjwt (io.jsonwebtoken)      |
| ORM | Spring Data JPA (Hibernate) |
| Database | MySQL                       |
| Build Tool | Gradle                      |
| 기타 | Lombok, MapStruct           |


## 📡 4. API 명세서
### API 명세서 요약
| 기능          | 메서드/경로                          | 설명                                        |
| ----------- | ------------------------------- |-------------------------------------------|
| 회원가입        | `POST /api/user/signup`         | 회원가입                                      |
| 로그인         | `POST /api/user/login`          | 로그인, JWT 발급                               |
| Access 재발급  | `POST /api/user/token`          | 로그인 유지를 위해 RefreshToken으로 AccessToken 재발급 |
| 내 정보 조회     | `GET /api/user/my`              | 로그인한 사용자 정보 조회                            |

### 회원가입
- **POST** `/api/user/signup`
- Request Body
  ````
  {
     "email": "test@example.com",
     "nickname": "테스트유저",
     "password": "Test1234!",  
     "name": "해시스냅",
     "phoneNumber": "010-1234-5678"
  }   
  ````
- Response Body
  ````
  {
    "status": 201,
    "message": "회원가입이 완료되었습니다.",
    "data": null
  }
  ````
### 로그인
- **POST** `/api/user/login`
- Request Body
  ````
  {
     "email": "test@example.com",
     "password": "Test1234!"
  }   
  ````
- Response Body
  ````
  {
    "status": 200,
    "message": "로그인에 성공하였습니다.",
    "data": {
        "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiZW1haWwiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNzUzOTg2ODI5LCJleHAiOjE3NTM5ODc3Mjl9.Lyujcfapjypvz4cQF9M0LcgQqQrY6kh3hlipkK0n62Q3z6paToNV0sjviLV55W_zciSg9MsPRJmRRBARFQxcjA",
        "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiZW1haWwiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNzUzOTg2ODI5LCJleHAiOjE3NTUxOTY0Mjl9.D6mWGpTdtHUxSzvS27iW6rofhpj8ZiQF48UBAwtgezn9F1u3abUtM9N_K0boDyPdEqTx14HzZ0BHS0gYdWITMw"
    }
  }
  ````

### Access Token 재발급
- **POST** `/api/user/token`
- Request Header

  | 필드명 | 타입 | 값 | 필수 여부 |
  | --- | --- | --- | -- |
  | `Cookie` | String | `refreshToken=eyJhbGciOi...` | 필수 |
- Response Body
  ````
  {
    "status": 200,
    "message": "토큰이 재발급되었습니다.",
    "data": {
        "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiZW1haWwiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNzUzOTg3MDI1LCJleHAiOjE3NTM5ODc5MjV9.zdTym9jFlGqrcvgzlPbXPQ0knqLqnSSqj2qsF2RB-73cw-KTj2vsAl8KWbxbYCo7-dS-yr1FRoLKyE_FsRGPDw",
        "refreshToken": null
    }
  }
  ````
### 내 정보 조회
- **GET** `/api/user/my`
- Request Header

  | 필드명 | 타입 | 값                            | 필수 여부 |
    | --- | --- |------------------------------| -- |
-   | `Authorization` | String | `Bearer eyJhbGc...`          | 필수 |
  | `Cookie` | String | `refreshToken=eyJhbGciOi...` | 필수 |
- Response Body
  ````
  {
    "status": 200,
    "message": "내 정보 조회에 성공하였습니다.",
    "data": {
        "email": "test@example.com",
        "nickname": "테스트유저",
        "name": "해시스냅",
        "phoneNumber": "010-1234-5678"
    }
  }
  ````
## ✅ 5. 구현 범위
- [x] 회원가입 기능
- [x] 로그인 및 JWT 기반 인증
- [x] 내 정보 조회 기능
- [ ] 비밀번호 재설정 기능

## 🔍 6. 특별히 신경 쓴 부분
- Refresh Token 저장 위치에 대한 보안 고려
  - XSS 방지를 위해 HTTPOnly Cookie에 저장
  - CSRF 방지를 위해 SameSite=Lax 속성 설정
  - 세션 관리 및 탈취 대응을 위해 서버 DB에도 저장하여 이중 관리
- Refresh Token 재사용 방지
  - 기존 토큰이 존재하는 경우 삭제 후 새 토큰 저장 → 1인 1토큰 보장
- 예외 처리 일원화
  - UserException 기반 커스텀 예외를 정의하여 도메인 중심의 예외 관리 구조 확립

## 💡 7. 추가 구현 사항
- Access Token 만료 후 로그인 유지 기능 
  - Refresh Token을 통한 Access Token 재발급 API 구현
- 비밀번호 유효성 검증 커스터마이징 
  - @Password 어노테이션으로 재사용 가능하게 설계 
  - 대문자/소문자/특수문자 포함 여부, 최소 길이 등 유연하게 설정 가능