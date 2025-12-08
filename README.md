# Up-Date

## Back-end 소개

- 다양한 챌린지 참여와 매일 인증을 중심으로 `사용자의 꾸준한 성장을 돕는 기록형` 프로젝트입니다.
- `Java`, `Spring Boot`기반으로 서버를 구현하고, `MySQL`로 db를 사용했습니다.
- 개발은 초기 프로젝트 설정부터, db 생성 및 연결, 서버 연결, 프론트엔드 연결까지 `직접 구현`했습니다.
- Spring MVC 기반의 계층형 아키텍처로 구현했습니다.

### 개발 인원 및 기간

- 개발기간 :  2024-09-22 ~ 2024-12-07
- 개발 인원 : 프론트엔드/백엔드 1명 (본인)

### 사용 기술 및 tools
- MySQL
- Java

### Front-end
- <a href="https://github.com/100-hours-a-week/3-hazel-full-FE.git">Front-end Github</a>

### 서비스 시연 영상
- <a href="https://drive.google.com/file/d/19dL0NSIBOIrQxFLYrJG0B6V92SwIbt8x/view?usp=drive_link">구글 드라이브</a>

### 폴더 구조
<details>
  <summary>폴더 구조 보기/숨기기</summary>
  <div markdown="1">
    
      ├── README.md
      ├── common
      │    ├── exception
      │    │    ├── ApiException.java
      │    │    ├── ErrorCode.java
      │    │    └── GlobalExceptionHandler.java
      │    └── response
      │         ├── ApiResponse.java
      │         └── ErrorResponse.java
      ├── confing
      │    ├── jwt
      │    │    ├── CustomAccessDeniedHandler.java
      │    │    ├── CustomAuthenticationEntrPoint.java
      │    │    ├── JwtAuthenticationFilter.java
      │    │    ├── JwtTokenProvider.java
      │    │    └── UserPrincipal.java
      │    └── SecurityConfig.java
      ├── controller
      │    ├── AuthController.java
      │    ├── ChallengeController.java
      │    ├── PostController.java
      │    └── UserController.java
      ├── dto
      │    ├── auth
      │    ├── challenge
      │    ├── post
      │    └── user
      ├── entity
      │    ├── Challenge.java
      │    ├── ChallengeCategory.java
      │    ├── Participation.java
      │    ├── Post.java
      │    ├── PostLike.java
      │    ├── RefreshToken.java
      │    ├── Role.java
      │    └── User.java
      ├── repository
      │    ├── ChallengeRepository.java
      │    ├── ChallengeSummeraryProjection.java
      │    ├── ParticipationRepository.java
      │    ├── PostLikeRepository.java
      │    ├── PostRepository.java
      │    ├── RefreshTokenRepository.java
      │    └── UserRepository.java
      ├── service
      │    ├── AuthService.java
      │    ├── ChallengeService.java
      │    ├── ParticipationService.java
      │    ├── PostService.java
      │    └── UserService.java
      └── UpdateApiApplication.java

  </div>
</details> 

<br/>

## 서버 설계
### 서버 구조
||controller|service|repository|Entity|
|:---|:---|:---|:---|:---|
|유저|userController|userService|jserRepository|user|
|챌린지|challengeController|challengeService|challengeRepository|challenge|
|참여|-|participationService|participationRepository|participation|
|포스트|postController|postService|postRepository, postLikeRepository|post, postLike|

### 구현 기능

#### Users
```
- 유저 CRUD 기능 구현
- 회원가입, 로그인, 비밀번호 변경 시 bcrypt로 비밀번호 암호화하여 처리
- 세션을 통해 유저 정보 저장, 로그아웃/회원탈퇴 시 세션 destroy
- 미들웨어를 통해 세션 정보가 있는 유저 요청만 처리
```

#### Challenges
```
- 게시글 CRUD 기능 구현
```

#### Posts
```
- 댓글 CRUD 기능 구현
```
<br/>

## 데이터베이스 설계
### 요구사항 분석
`유저 관리`
- 사용자는 이메일, 프로필 이미지, 비밀번호, 닉네임 정보를 포함하는 유저 관리
- 각 유저는 고유한 식별자를 가지고 있으며, 이메일과 닉네임은 유니크하게 설정하여 중복 방지

`게시글 관리`
- 사용자가 제목, 내용, 이미지, 작성일시, 수정일시 등의 정보를 포함하는 게시글 관리
- 게시글은 작성자를 참조하여 관계를 설정

`댓글 관리`
- 사용자가 내용, 작성자, 작성일시 등의 정보를 포함하는 댓글 관리
- 댓글은 어떤 게시글에 속해 있는지 나타내는 참조 포함

`세션 관리`
- 사용자의 로그인 세션을 관리
- 세션 식별자, 만료 시간, 세션 데이터를 저장하여 사용자의 세션 추적

### 모델링
`E-R Diagram`  
요구사항을 기반으로 모델링한 E-R Diagram입니다.  
<br/>
<img src="https://github.com/100-hours-a-week/5-erica-express-be/assets/81230764/1546793d-fd03-47f3-8ed1-449edb764750" width="70%" />

<br/>

## 트러블 슈팅
추후 작성..

<br/>

## 프로젝트 후기
추후 작성..


<br/>
<br/>
<br/>

<p align="center">
  <img src="https://github.com/100-hours-a-week/5-erica-react-fe/assets/81230764/d611b233-b596-4d1d-bbb9-dc2e4e41eb47" style="width:200px; margin: 0 auto"/>
</p>
