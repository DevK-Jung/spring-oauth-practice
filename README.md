# Spring OAuth 연습 프로젝트

이 프로젝트는 **Spring Boot 3** 기반으로 **Google**, **Kakao**, **Naver**의 OAuth2 로그인을 통합하는 방법을 보여줍니다.  
최초 로그인 시 사용자 정보를 **MySQL 데이터베이스에 저장**하고, 추가 프로필 정보를 요청하도록 구성되어 있습니다.

## 주요 기능

- **Google**, **Kakao**, **Naver** OAuth2 로그인
- 커스텀 파서를 통한 인증된 `OAuthUser` 객체 자동 생성
- 신규 사용자에게 **추가 정보 입력 페이지**(닉네임, 나이) 제공
- **JPA + MySQL**을 사용한 사용자 및 권한 정보 저장
- **Thymeleaf**를 활용한 로그인 및 회원가입 페이지

## 시작하기

### 사전 준비

- Java 21
- [Gradle](https://gradle.org/) (wrapper 포함)
- Docker (`db-docker-compose.yml`로 MySQL 실행용)

### 데이터베이스 설정

Docker로 로컬 MySQL 인스턴스를 실행하세요:

```bash
docker compose -f db-docker-compose.yml up -d
```

MySQL은 `localhost:33064`에서 실행되며, 사용자 `root` / 비밀번호 `root1234`,  
데이터베이스명은 `spring_oauth`입니다.

### OAuth 인증 정보 설정

OAuth 클라이언트 정보를 환경 변수로 설정해야 합니다.  
`.env` 파일을 생성하거나 다음 변수를 export 하세요:

```bash
export google_client_id=YOUR_GOOGLE_CLIENT_ID
export google_client_secret=YOUR_GOOGLE_CLIENT_SECRET
export kakao_client_id=YOUR_KAKAO_CLIENT_ID
export kakao_client_secret=YOUR_KAKAO_CLIENT_SECRET
export naver_client_id=YOUR_NAVER_CLIENT_ID
export naver_client_secret=YOUR_NAVER_CLIENT_SECRET
```

### 애플리케이션 실행

Gradle Wrapper를 사용하여 애플리케이션을 실행하세요:

```bash
sh gradlew bootRun
```

서버는 [http://localhost:8099](http://localhost:8099) 에서 시작되며,  
`/login` 페이지에서 OAuth 로그인 기능을 확인할 수 있습니다.

### 테스트

단위 테스트 실행:

```bash
sh gradlew test
```

(*테스트는 위에서 실행한 MySQL 인스턴스에 연결되어야 합니다.*)

## 프로젝트 구조

- `src/main/java/com/kjung/springoauth/app`  
  `account`, `login`, `home`, `user`, `role` 등 도메인 패키지
- `src/main/java/com/kjung/springoauth/core`  
  공통 유틸리티 및 보안 설정 패키지
- `src/main/resources`  
  애플리케이션 설정 및 Thymeleaf 템플릿

## 라이선스

이 프로젝트는 학습 및 실습용으로 제작되었습니다.