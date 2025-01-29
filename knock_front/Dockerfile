# 1. 빌드 단계
FROM node:20-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /app

# 패키지 파일을 복사
COPY ./package.json ./

# NPM INSTALL
RUN npm install

# 소스 파일 복사
COPY . .

# Next.js 빌드
RUN npm run build

## 2. 실행 단계
FROM node:20-alpine

WORKDIR /app

COPY --from=builder /app ./

EXPOSE 3000

CMD ["npm", "run", "start"]

### 2. 프로덕션 단계
#FROM nginx
#
## Nginx에서 사용할 포트 열기
#EXPOSE 3000
#
## Nginx 설정 파일 복사
#COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf
#
## Next.js 빌드 결과물 복사 (빌드된 파일을 Nginx의 HTML 디렉토리에 복사)
#COPY --from=builder /app/build  /usr/share/nginx/html

