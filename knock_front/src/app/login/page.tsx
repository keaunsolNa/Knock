import Link from 'next/link';
import styles from './page.module.scss';

export default function Page() {
  return (
    <div className={styles.div__login}>
      <span className={styles.span__title}>SNS 계정으로 간편 로그인</span>

      <div className={styles.button__list}>
        <Link href={`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/kakao`}>
          <div className={styles.btn__kakao}>
            <img src="/login/kakao_logo.png" />
            <span>카카오 계정으로 로그인</span>
          </div>
        </Link>
        <Link href={`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/naver`}>
          <div className={styles.btn__naver}>
            <img src="/login/naver_logo.png" />
            <span>네이버 계정으로 로그인</span>
          </div>
        </Link>
        <Link href={`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/google`}>
          <div className={styles.btn__google}>
            <img src="/login/google_logo.png" />
            <span>구글 계정으로 로그인</span>
          </div>
        </Link>

        <span className={styles.btn__guest}>게스트로 시작하기</span>
      </div>
    </div>
  );
}
