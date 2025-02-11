'use client';

import { useEffect, useState } from 'react';
import styles from './page.module.scss';
import { BeatLoader } from 'react-spinners';
import { useDispatch, useSelector } from 'react-redux';
import { setAccessToken } from '@/redux/authSlice';
import { VscDebugRestart } from 'react-icons/vsc';
import Link from 'next/link';
import { RootState } from '@/redux/store';
import { useRouter } from 'next/navigation';

/**
 * 
 * 백-프론트 통신과정

(/login)
프론트 로그인 요청 ㅡ> 백 로그인   확인 ㅡ> JWT토큰 생성 / Refresh 토큰 HttpOnly 쿠키로 설정  ㅡ> 프론트 콜백페이지(/login/auth)

(/login/auth)
프론트 콜백페이지 ㅡ> Access 토큰 요청. fetch 보낼 때 Refresh 토큰 자동전송 ㅡ>  백에서 Access 토큰 body에 담아 response ㅡ> 프론트에서 Redux로 Access Token 저장  ㅡ>  프론트 메인 페이지
이후 모든 데이터 fetch 시 Access token 담아서 요청
 */

export default function Page() {
  const router = useRouter();
  const dispatch = useDispatch();
  const [error, setError] = useState(false);
  const accessToken = useSelector((state: RootState) => state.auth.accessToken);

  const getAccessToken = async () => {
    setError(false);

    try {
      // Access 토큰 요청. Refresh Token 포함.
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/getAccessToken`,
        {
          method: 'POST',
          credentials: 'include',
        }
      );

      console.log(response);

      if (!response.ok) {
        if (response.status >= 400 && response.status < 500) {
          throw new Error(`${response.status} : 클라이언트 오류`);
        } else {
          throw new Error(`${response.status} : 서버 오류`);
        }
      }

      const { accessToken } = await response.json();
      console.log('Access Token:', accessToken);

      dispatch(setAccessToken(accessToken));
    } catch (err) {
      console.error(err);
      setError(true);
    }
  };

  useEffect(() => {
    getAccessToken();
  }, []);

  useEffect(() => {
    if (accessToken) {
      console.log(accessToken);
      router.push('/'); // accessToken이 세팅되면 리다이렉션
    }
  }, [accessToken]);

  return (
    <div className={styles.container}>
      {error ? (
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
