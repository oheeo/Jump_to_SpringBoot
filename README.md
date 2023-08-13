<h2 align="center">스프링부트를 이용한 게시판 만들기</h2>

![게시판](https://github.com/oheeo/Jump_to_SpringBoot/assets/122732781/9238abea-118e-42ae-b0d2-eb9b988ed858)

### 프로젝트 기간
2023.07.06 ~ 2023.07.08

### 목적
스프링부트를 이용해서 전반적인 웹의 기본 소양이 되는 CRUD 게시판을 만들고, 기능을 하나씩 추가하는 방식으로 진행했습니다.

### 인원 구성
혼자 진행했습니다.

### 기능
- 회원가입
- 로그인/ 로그아웃
- 게시글 등록/ 수정/ 삭제
- 댓글 등록/ 수정/ 삭제
- 작성자 표시
- 수정날짜 표시
- 추천
- 검색
- 앵커
- 네비게이션 바
- 게시글 목록 페이징
- 댓글 개수 표시
- 스프링 시큐리티를 통한 보안
- 마크다운

### 사용 기술
- JAVA 17
- Spring Boot 3
- Gradle
- Lombok
- Bootstrap
- Spring Security
- Thymeleaf
- JPA
- H2
- Junit
- Spring Data JPA

### API 설계
- 회원 관련 API

|기능|Method|URL|Return Page|
|---|---|---|---|
|회원가입 페이지|`GET`|/user/signup|회원가입 페이지로 이동|
|회원가입|`POST`|/user/signup|회원가입 후 전체 목록 페이지로 이동|
|로그인 페이지|`GET`|/user/login|로그인 페이지로 이동|
|로그아웃|`GET`|/user/logout|로그아웃 후 전체 목록 페이지로 이동|

- 게시글 관련 API

|기능|Method|URL|Return Page|
|---|---|---|---|
|게시글 전체 목록 조회|`GET`|/question/list|게시글 전체 목록|
|게시글 페이징 목록 조회|`GET`|/question/list?page={번호}|게시글 페이징 목록|
|게시글 상세 페이지|`GET`|/question/detail/{id}|게시글 상세 페이지로 이동|
|게시글 등록 페이지|`GET`|/question/create|게시글 등록 페이지로 이동|
|게시글 수정 페이지|`GET`|/question/modify/{id}|게시글 수정 페이지로 이동|
|게시글 검색|`GET`|/question/list?kw={검색어}|게시글 검색|
|게시글 검색 후 페이징|`GET`|/question/list?kw={검색어}&page={번호}|게시글 검색 후 페이징 목록|
|게시글 등록|`POST`|/question/create|게시글 등록 후 목록 페이지로 이동|
|게시글 수정|`POST`|/question/modify/{id}|게시글 수정 후 게시글 상세 페이지로 이동|
|게시글 삭제|`GET`|/question/delete/{id}|게시글 삭제 후 전체 목록 페이지로 이동|
|게시글 추천|`GET`|/question/vote/{id}|게시글 추천 후 게시글 상세 페이지로 이동|

- 댓글 관련 API

|기능|Method|URL|
|---|---|---|
|댓글 등록|`POST`|/answer/create/{id}|
|댓글 수정 페이지|`GET`|/answer/modify/{id}|
|댓글 수정|`POST`|/answer/modify/{id}|
|댓글 삭제|`GET`|/answer/delete/{id}|
|댓글 추천|`GET`|/answer/vote/{id}|

<br>

#### 게시판 목록
<img src="https://github.com/oheeo/Jump_to_SpringBoot/assets/122732781/df2d61cf-7c03-4476-ab9c-bf5b5090ef5f">

#### 게시물
<img src="https://github.com/oheeo/Jump_to_SpringBoot/assets/122732781/703c4bd3-c3e0-4e07-8a89-1aa344a25c32">

#### H2 데이터페이스 Table
<img src="https://github.com/oheeo/Jump_to_SpringBoot/assets/122732781/0cee5d18-88d8-4ff9-9609-7c6c11cd029a">

