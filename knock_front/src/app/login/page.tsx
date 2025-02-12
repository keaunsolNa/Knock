'use client';

import { useState } from 'react';
import styles from './page.module.scss';
import { setAccessToken } from '@/redux/authSlice';
import { BeatLoader } from 'react-spinners';
import { VscDebugRestart } from 'react-icons/vsc';
import { useAppDispatch } from '@/redux/store';

/**
 * 
 * 백-프론트 통신과정

(/login)
프론트 로그인 요청 ㅡ> 백 로그인 확인 ㅡ> JWT토큰 생성 / Refresh 토큰 HttpOnly 쿠키로 설정  ㅡ> 프론트 콜백페이지(/login)

(callback)
프론트 accessToken 확인 ㅡ> redux에 저장 ㅡ> 저장 확인 후 redirect 페이지로 이동
 */

export default function Page() {
  const dispatch = useAppDispatch();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(false);

  const handleOAuthLogin = async (social: string) => {
    setIsLoading(true);

    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/${social}`,
        {
          method: 'GET',
          credentials: 'include', // ✅ 쿠키 저장 허용
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
      setError(true);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      {error ? (
        <section className={styles.section__error}>
          <p className={styles.p__error}>
            서버와의 통신에 문제가 발생했습니다.
          </p>
          <div className={styles.btn__retry} onClick={() => setError(false)}>
            <VscDebugRestart />
          </div>
        </section>
      ) : isLoading ? (
        <section className={styles.section__loading}>
          <BeatLoader size={30} color={'#f45f41'} />
          <p>로그인 중...</p>
        </section>
      ) : (
        <section className={styles.section__login}>
          <span className={styles.span__title}>SNS 계정으로 간편 로그인</span>

          <div className={styles.button__list}>
            <div
              className={styles.btn__kakao}
              onClick={() => handleOAuthLogin(`kakao`)}
            >
              <img src="/login/kakao_logo.png" />
              <span>카카오 계정으로 로그인</span>
            </div>
            <div
              className={styles.btn__naver}
              onClick={() => handleOAuthLogin(`naver`)}
            >
              <img src="/login/naver_logo.png" />
              <span>네이버 계정으로 로그인</span>
            </div>
            <div
              className={styles.btn__google}
              onClick={() => handleOAuthLogin(`google`)}
            >
              <img src="/login/google_logo.png" />
              <span>구글 계정으로 로그인</span>
            </div>
          </div>
        </section>
      )}
    </div>
  );
}
