'use client';

import { BeatLoader } from 'react-spinners';
import styles from './page.module.scss';
import { useEffect, useState } from 'react';
import { useParams, useRouter, useSearchParams } from 'next/navigation';
import { useAppDispatch } from '@/redux/store';
import { setAuth } from '@/redux/authSlice';
import { VscDebugRestart } from 'react-icons/vsc';
import Link from 'next/link';

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
    dispatch(setAuth(data));
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
    dispatch(setAuth(data));
    router.push(data.redirectUrl);
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
