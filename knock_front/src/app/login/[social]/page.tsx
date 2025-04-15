'use client';

import styles from './page.module.scss';
import { BeatLoader } from 'react-spinners';
import { useParams, useRouter } from 'next/navigation';
import { useEffect } from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '@/redux/store';

export default function Page() {
  const { social } = useParams();
  const router = useRouter();
  const { accessToken, redirectUrl } = useSelector((state: RootState) => state.auth);

  useEffect(() => {
    if (accessToken) {
      router.replace(redirectUrl);
    } else {
      if (social === 'kakao') {
        const baseUrl = 'https://kauth.kakao.com/oauth/authorize?';
        const params = [
          'response_type=code',
          `client_id=${process.env.NEXT_PUBLIC_KAKAO_CLIENT_ID}`,
          `redirect_uri=${process.env.NEXT_PUBLIC_KAKAO_REDIRECT_URI}`,
        ];
        router.replace(baseUrl + params.join('&'));
      } else if (social === 'naver') {
        const baseUrl = 'https://nid.naver.com/oauth2.0/authorize?';
        const params = [
          `response_type=code`,
          `client_id=${process.env.NEXT_PUBLIC_NAVER_CLIENT_ID}`,
          `redirect_uri=${process.env.NEXT_PUBLIC_NAVER_REDIRECT_URI}`,
        ];
        router.replace(baseUrl + params.join('&'));
      } else if (social === 'google') {
        const baseUrl = 'https://accounts.google.com/o/oauth2/auth?';
        const params = [
          `scope=openid https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email`,
          `response_type=code`,
          `client_id=${process.env.NEXT_PUBLIC_GOOGLE_CLIENT_ID}`,
          `redirect_uri=${process.env.NEXT_PUBLIC_GOOGLE_REDIRECT_URI}`,
        ];
        router.replace(baseUrl + params.join('&'));
      } else {
        router.replace('/login'); // kakao,naver,google 제외한 소셜로그인 접근시 로그인 페이지로 이동
      }
    }
  }, [social]);

  return (
    <div className={styles.container}>
      <BeatLoader size={30} color={'#f45f41'} />
      <p>로그인 중...</p>
    </div>
  );
}
