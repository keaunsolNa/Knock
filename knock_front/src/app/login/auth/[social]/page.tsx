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

  const getJwtToken = async () => {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/login/${social}/callback`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({ authorizationCode }),
    });

    if (!response.ok) {
      setIsError(true);
      throw new Error(`${response.status} : ${response.statusText}`);
    }

    const data = await response.json();

    dispatch(setAuth({ accessToken: data.accessToken.value, redirectUrl: data.redirectUrl }));
    await handleDeviceToken();
    router.push(data.redirectUrl);
  };

  const handleGoogleLogin = async () => {
    const accessTokenResponse = await fetch(
      `https://oauth2.googleapis.com/token?code=${authorizationCode}&client_id=${process.env.NEXT_PUBLIC_GOOGLE_CLIENT_ID}&client_secret=${process.env.NEXT_PUBLIC_GOOGLE_CLIENT_SECRET}&redirect_uri=${process.env.NEXT_PUBLIC_GOOGLE_REDIRECT_URI}&grant_type=authorization_code`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      }
    );

    if (!accessTokenResponse.ok) {
      throw new Error(`${accessTokenResponse.status} : ${accessTokenResponse.statusText}`);
    }

    const { access_token } = await accessTokenResponse.json();

    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/login/${social}/callback`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({ authorizationCode: access_token }),
    });

    if (!response.ok) {
      setIsError(true);
      throw new Error(`${response.status} : ${response.statusText}`);
    }

    const data = await response.json();
    dispatch(setAuth({ accessToken: data.accessToken.value, redirectUrl: data.redirectUrl }));
    await handleDeviceToken();
    router.push(data.redirectUrl);
  };

  const handleDeviceToken = async () => {
    if (Notification.permission !== 'granted') return;

    try {
      const currentToken = await getToken(messaging, { vapidKey: process.env.NEXT_PUBLIC_VAPID_KEY });

      if (currentToken) {
        const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/saveToken`, dispatch, {
          method: 'POST',
          body: JSON.stringify({
            targetToken: currentToken,
          }),
        });

        if (!response.ok) {
          throw new Error('토큰 저장 실패');
        }
      } else {
        alert('푸시 알림 토큰을 가져오는 데 실패했습니다.');
      }
    } catch (err) {
      alert('토큰을 가져오는 중 오류가 발생했습니다. 다시 시도해주세요.');
      console.log(err);
    }
  };

  useEffect(() => {
    if (social === 'google') {
      handleGoogleLogin();
    } else {
      getJwtToken();
    }
  }, [social, authorizationCode]);

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
