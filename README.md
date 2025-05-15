[바로인턴_Java 과제.postman_collection.json](https://github.com/user-attachments/files/20220616/_Java.postman_collection.json)# 📌 Spring Boot 기반 JWT 인증/인가 및 AWS 배포

## 📖 프로젝트 설명
### 1. USER 권한 회원가입
ADMIN 계정은 1개 초기 설정(.env, AdminInitializer)
### 2. 회원 로그인
### 3. ADMIN 권한 부여
ADMIN 권한 로그인 시에만 다른 계정 권한을 USER → ADMIN 부여할 수 있음
<br>

## ⭐️ API 엔드포인트
| HTTP 메소드 | 엔드포인트 | 기능 |
|---|:---:|---|
| `POST` | /signup | 회원가입 |
| `POST` | /login | USER 권한 계정 로그인 |
| `PATCH` | /admin/users/{userId}/roles | ADMIN 권한 부여 |
<br>

## 🚀 실행 방법
### 로컬
1. src/main/java/com/example/task/TaskApplication.java 실행
2. Swagger UI(http://localhost:8080/swagger-ui/index.html) 접속
3. USER1 회원가입 실행
4. ADMIN 로그인(username:admin, password:admin1234!) 실행 - *응답 token 값 복사*
5. Swagger UI 우측 상단 bearerAuth 클릭하여 토큰으로 로그인
6. 관리자 권한 부여 실행(userId:2) → USER에서 ADMIN으로 변경

### 배포
- 배포 host : 
- Swagger UI 주소 : 배포 host/swagger-ui/index.html
<br>

## 📚 API 문서 (Swagger)
- **Swagger**: [API 문서](https://sparta-delivery-9zo.p-e.kr/swagger-ui/index.html)
- **Postman**: [Uploading 바로인턴_Java 과제.postman_collection.json…]()

<br>
