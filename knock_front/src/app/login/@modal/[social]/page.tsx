'use client';

import { useParams, useRouter } from 'next/navigation';
import { useEffect } from 'react';

export default function Page() {
  const { social } = useParams();
  const router = useRouter();

  useEffect(() => {
    if (social === 'kakao') {
      const baseUrl = 'https://kauth.kakao.com/oauth/authorize?';
      const params = [
        'response_type=code',
        `client_id=${process.env.NEXT_PUBLIC_KAKAO_CLIENT_ID}`,
        `redirect_uri=${process.env.NEXT_PUBLIC_KAKAO_REDIRECT_URI}`,
      ];
      window.location.href = baseUrl + params.join('&');
    } else if (social === 'naver') {
      const baseUrl = 'https://nid.naver.com/oauth2.0/authorize?';
      // TODO : naver state 추가 필요
      const params = [
        `response_type=code`,
        `client_id=${process.env.NEXT_PUBLIC_NAVER_CLIENT_ID}`,
        `redirect_uri=${process.env.NEXT_PUBLIC_NAVER_REDIRECT_URI}`,
      ];
      window.location.href = baseUrl + params.join('&');
    } else if (social === 'google') {
      const baseUrl = 'https://accounts.google.com/o/oauth2/auth?';
      const params = [
        `response_type=code`,
        `scope=openid https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email`,
        `client_id=${process.env.NEXT_PUBLIC_GOOGLE_CLIENT_ID}`,
        `redirect_uri=${process.env.NEXT_PUBLIC_GOOGLE_REDIRECT_URI}`,
      ];
      window.location.href = baseUrl + params.join('&');
    } else {
      router.replace('/login'); // kakao,naver,google 제외한 소셜로그인 접근시 로그인 페이지로 이동
    }
  }, [social]);

  return null;
}
