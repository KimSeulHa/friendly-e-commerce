### e-commerce project

Use skill: Spring, Jpa, Mysql, Redis, Docker, AWS

Goal: seller와 customer를 이루는 플랫폼 형식의 커머스 서버를 구축한다.

## Common
- [x] 이메일로 인증번호 발송 후 회원가입 진행 -> mailgun api 사용

## Customer
- [x] 회원 가입
- [x] 이메일 인증
- [x] 로그인 토큰 발행 -> JWT 
- [x] 로그인 토큰을 통해 일부 url 제어 (JWT, Filter)

## Seller
- [x] 회원 가입
- [x] 이메일 인증

## Order
- [x] 상품 CRUD
- [x] Redis를 활용한 장바구니 등록 및 조회
- [x] 장바구니 수정
- [ ] 장바구니 내용 메일 발 
