# 1. 빌드 단계
FROM node:20 as builder

# 작업 디렉토리 설정
WORKDIR /app

# 패키지 파일을 복사하고 의존성 설치
COPY ./package.json ./package-lock.json ./
RUN npm install

# Next.js 빌드 (빌드 결과물은 /app/.next 디렉토리)
COPY . .
RUN npm run build

# Next Port
EXPOSE 3000

# 2. 프로덕션 단계
FROM nginx

# Nginx에서 사용할 포트 열기
EXPOSE 80

# Nginx 설정 파일 복사
COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf

# Next.js 빌드 결과물 복사 (빌드된 파일을 Nginx의 HTML 디렉토리에 복사)
COPY --from=builder /app/out /usr/share/nginx/html

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]
