'use client';

import styles from './page.module.scss';

function LoginIntro() {
  return (
    <section className={styles.intro}>
      <p className={styles.span_intro}>
        <span className={styles.span__category}>
          영화, 뮤지컬,
          <br />
          오페라, 전시회
        </span>
        를 <br />
        사랑하는 당신을 위한
        <span className={styles.span__point}> 티켓팅 헬퍼</span>
      </p>

      <div className={styles.div__logo}>
        <span className={styles.span__point}>K</span>
        <span>NOCK</span>
      </div>
    </section>
  );
}

export default function Page() {
  const kakaoOnclick = async () => {
    window.location.href = `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/kakao`;
  };
  const naverOnclick = async () => {
    window.location.href = `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/naver`;
  };
  const googleOnclick = async () => {
    window.location.href = `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/google`;
  };

  return (
    <div className={styles.container}>
      <LoginIntro />

      <section className={styles.section__login}>
        <span className={styles.section__title}>SNS 계정으로 간편 로그인</span>

        <div className={styles.button__list}>
          <div className={styles.btn__kakao} onClick={kakaoOnclick}>
            <img src="/login/kakao_logo.png" />
            <span>카카오 계정으로 로그인</span>
          </div>
          <div className={styles.btn__naver} onClick={naverOnclick}>
            <img src="/login/naver_logo.png" />
            <span>네이버 계정으로 로그인</span>
          </div>
          <div className={styles.btn__google} onClick={googleOnclick}>
            <img src="/login/google_logo.png" />
            <span>구글 계정으로 로그인</span>
          </div>

          <span className={styles.btn__guest}>게스트로 시작하기</span>
        </div>
      </section>
    </div>
  );
}
