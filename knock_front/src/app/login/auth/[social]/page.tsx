'use client';

import { BeatLoader } from 'react-spinners';
import styles from './page.module.scss';
import { useEffect, useState } from 'react';
import { useParams, useRouter, useSearchParams } from 'next/navigation';
import { useAppDispatch } from '@/redux/store';
import { setAuth } from '@/redux/authSlice';
import { VscDebugRestart } from 'react-icons/vsc';
import Link from 'next/link';
import { getToken } from 'firebase/messaging';
import { messaging } from '@/utils/firebaseConfig';
import { apiRequest } from '@/utils/api';

export default function Page() {
  const router = useRouter();
  const dispatch = useAppDispatch();
  const { social } = useParams();
  const authorizationCode = useSearchParams().get('code');
  const [isError, setIsError] = useState(false);

  useEffect(() => {
    if (!authorizationCode || !social) return;
    getJwtToken();
  }, [social, authorizationCode]);

  /**
   * JWT accessToken 불러오는 전체로직
   */
  const getJwtToken = async () => {
    try {
      const authorizationCode = await fetchAuthorization();
      const { accessToken, redirectUrl } = await fetchAccessToken(authorizationCode);

      dispatch(setAuth({ accessToken: accessToken.value, redirectUrl: redirectUrl }));
      await registerDeviceToken();

      router.replace(redirectUrl);
    } catch (error) {
      console.error(error);
      setIsError(true);
    }
  };

  /**
   * google 인증코드 불러오기
   */
  const fetchAuthorization = async () => {
    if (social !== 'google') return authorizationCode;

    const params = new URLSearchParams({
      code: authorizationCode,
      client_id: process.env.NEXT_PUBLIC_GOOGLE_CLIENT_ID,
      client_secret: process.env.NEXT_PUBLIC_GOOGLE_CLIENT_SECRET,
      redirect_uri: process.env.NEXT_PUBLIC_GOOGLE_REDIRECT_URI,
      grant_type: 'authorization_code',
    });

    const response = await fetch(`https://oauth2.googleapis.com/token?${params.toString()}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });

    if (!response.ok) {
      throw new Error(`${response.status} : ${response.statusText}`);
    }

    const { access_token } = await response.json();
    return access_token;
  };

  /**
   * 백엔드 AccessToken 불러오기
   */
  const fetchAccessToken = async (code: string) => {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/login/${social}/callback`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({ authorizationCode: code }),
    });

    if (!response.ok) {
      throw new Error(`${response.status} : ${response.statusText}`);
    }

    return response.json();
  };

  /**
   * FCM DeviceToken 등록
   */
  const registerDeviceToken = async () => {
    if (Notification.permission !== 'granted') return;

    try {
      // FCM 디바이스 토큰 불러오기
      const currentToken = await getToken(messaging, { vapidKey: process.env.NEXT_PUBLIC_VAPID_KEY });

      if (!currentToken) {
        alert('알림 권한이 없으면 개봉 알림을 받으실 수 없어요!');
        return;
      }

      const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/saveToken`, dispatch, {
        method: 'POST',
        body: JSON.stringify({
          targetToken: currentToken,
        }),
      });

      if (!response.ok) {
        throw new Error('FCM 토큰 저장 실패');
      }
    } catch (err) {
      console.log('FCM 토큰 처리 실패 : ', err);
    }
  };

  return (
    <div className={styles.container}>
      {isError ? (
        <>
          <p className={styles.p__error}>서버와의 통신에 문제가 발생했습니다.</p>
          <Link href={`/login`}>
            <div className={styles.btn__retry}>
              <VscDebugRestart />
            </div>
          </Link>
        </>
      ) : (
        <>
          <BeatLoader size={30} color={'#f45f41'} />
          <p>로그인 중...</p>
        </>
      )}
    </div>
  );
}
