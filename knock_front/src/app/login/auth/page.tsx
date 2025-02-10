'use client';

import { useEffect } from 'react';
import styles from './page.module.scss';
import { BeatLoader } from 'react-spinners';

/**
 * 
 * 백-프론트 통신과정

(/login)
프론트 로그인 요청 ㅡ> 백 로그인   확인 ㅡ> JWT토큰 생성 / Refresh 토큰 HttpOnly 쿠키로 설정  ㅡ> 프론트 콜백페이지(/login/auth)

(/login/auth)
프론트 콜백페이지 ㅡ> Access 토큰 요청. fetch 보낼 때 Refresh 토큰 자동전송 ㅡ>  백에서 Access 토큰 body에 담아 response ㅡ> 프론트에서 Redux로 Access Token 저장  ㅡ>  프론트 메인 페이지
이후 모든 데이터 fetch 시 Access token 담아서 요청
 */

const getAccessToken = async () => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/getAccessToken`,
    {
      method: 'POST',
      credentials: 'include',
    }
  );
  if (!response.ok) {
    return <div>통신오류</div>;
  }

  const data = await response.json();

  console.log(data);
};

export default function Page() {
  useEffect(() => {
    getAccessToken();
  }, []);

  return (
    <div className={styles.container}>
      <BeatLoader size={30} color={'#f45f41'} />
      <p>로그인 중...</p>
    </div>
  );
}
