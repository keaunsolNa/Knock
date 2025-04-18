# KNOCK?

영화 및 공연예술의 개봉일 리마인드 서비스. 
구독한 컨텐츠의 개봉일이 다가오면 알림(Push)을 보내주는 **컨텐츠 리마인더**입니다.

![image](https://github.com/user-attachments/assets/8ed878b7-0747-4ffd-acaf-f04b2cd6b64a)

<p align="center">
  <img src="https://github.com/user-attachments/assets/9c1c74c8-5488-4206-8988-6fdc50754d86" width="31%" />
  <img src="https://github.com/user-attachments/assets/ad0710f9-5dc3-472e-9748-6d0d7e2043ec" width="31%" />
  <img src="https://github.com/user-attachments/assets/fba915e7-b273-41f0-8809-4c8641ee78b4" width="31%" />
</p>

## 👥 팀원

---

- **Front-End** : **원예진** —> https://github.com/wonyj0228
- **Back-End**  : **나큰솔** —> https://github.com/keaunsolNa

## ⌛ 개발 기간

---

- **전체 기간** : 2025.01.01 ~ 2025.04.04 (**총 3개월**)
- **기획 설계 및 디자인** : 2025.01.01 ~ 2025.01.21 / 2025.03.01 ~ 2025.03.14  (**약 1달**)
- **개발 기간** : 2025.01.22 ~ 2025.02.28 / 2025.03.15 ~ 2025.04.04 (**약 2달**)

## 👨‍💻 기술 스택

---

**Front-End**

- Next.js
- TypeScript
- Redux / Redux Tool-kit
- PWA / FCM (Firebase)
- SCSS
- Vercel

**Back-End**

- Spring Boot Security
- Elasticsearch (로컬)
- BonsAi (배포)
- Selenium
- FCM (Firebase)
- Heroku

더 자세한 기술은 [개발 환경 페이지](https://www.notion.so/1c3eb6c84ddd80f597d8efe374f69bab?pvs=21)에서 확인해주세요!

## 🛎️ 주요 기능

---

> **SSO 소셜 로그인 (JWT)**
> 
- JWT 토큰을 활용한 카카오/네이버/구글 소셜 로그인 기능
- JWT + HttpOnly / Secure Cookie + Redux

> **개봉 예정 컨텐츠 크롤링 및 조회**
> 
- 영화(KOFIC, CGV, MEGABOX, LOTTE CINEMA) / 공연예술(KOPIS) 에서 데이터 크롤링
- 개봉 예정인 컨텐츠 조회 및 검색 기능
- 유저 데이터에 기반한 추천 컨텐츠 조회

> **컨텐츠 구독**
> 
- 개봉 예정 알림을 받고싶은 컨텐츠 구독 / 구독 취소

> **알림 설정 / 모바일 푸시(Push) 알림**
> 
- 개봉일로부터 D-? 로 알림 설정
- 유저가 설정해둔 디데이 날짜로 오전 9시에 모바일 푸시(Push) 알림
- PWA + FCM (디바이스 토큰)

더 자세한 기능은 [주요기능 페이지](https://www.notion.so/1d0eb6c84ddd8071800ad9cf7a407c55?pvs=21)에서 확인해주세요!
