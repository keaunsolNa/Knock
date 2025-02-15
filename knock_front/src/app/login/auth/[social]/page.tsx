'use client';

import { BeatLoader } from 'react-spinners';
import styles from './page.module.scss';
import { useEffect, useState } from 'react';
import { useParams, useSearchParams } from 'next/navigation';
import { useAppDispatch } from '@/redux/store';
import { setAccessToken } from '@/redux/authSlice';
import { VscDebugRestart } from 'react-icons/vsc';
import Link from 'next/link';

export default function Page() {
  const { social } = useParams();
  const authorizationCode = useSearchParams().get('code');
  const dispatch = useAppDispatch();
  const [isError, setIsError] = useState(false);

  const getJwtToken = async () => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/${social}/callback?authorizationCode=${authorizationCode}`,
        {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
          credentials: 'include',
        }
      );

      if (!response.ok) {
        throw new Error(`${response.status} : ${response.statusText}`);
      }
      const { accessToken, redirect_url } = await response.json();
      dispatch(setAccessToken(accessToken));
      window.location.href = redirect_url;
    } catch (error) {
      console.error('----OAuth 로그인 실패----');
      setIsError(true);
    }
  };

  useEffect(() => {
    getJwtToken();
  }, [social, authorizationCode]);

  return (
    <div className={styles.container}>
      {isError ? (
        <>
          <p className={styles.p__error}>
            서버와의 통신에 문제가 발생했습니다.
          </p>
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
