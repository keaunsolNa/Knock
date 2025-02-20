'use client';

import { BeatLoader } from 'react-spinners';
import styles from './page.module.scss';
import { useEffect, useState } from 'react';
import { useParams, useRouter, useSearchParams } from 'next/navigation';
import { useAppDispatch } from '@/redux/store';
import { setAccessToken } from '@/redux/authSlice';
import { VscDebugRestart } from 'react-icons/vsc';
import Link from 'next/link';

export default function Page() {
  const router = useRouter();
  const dispatch = useAppDispatch();
  const { social } = useParams();
  const authorizationCode = useSearchParams().get('code');
  const [isError, setIsError] = useState(false);

  const getJwtToken = async () => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/login/${social}/callback`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          credentials: 'include',
          body: JSON.stringify({ authorizationCode }),
        }
      );

      if (!response.ok) {
        throw new Error(`${response.status} : ${response.statusText}`);
      }

      const data = await response.json();
      console.log(data);
      dispatch(setAccessToken(data.accessToken.value));
      router.push(data.redirectUrl);
    } catch (error) {
      console.log(error);
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
