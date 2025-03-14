import Link from 'next/link';
import styles from './page.module.scss';

export default function Page() {
  return (
    <div className={styles.container}>
      <section className={styles.section__login}>
        <span className={styles.span__title}>SNS 계정으로 간편 로그인</span>

        <div className={styles.button__list}>
          <Link href={'/login/kakao'}>
            <div className={styles.btn__kakao}>
              <img src="/logo/kakao.png" />
              <span>카카오 계정으로 로그인</span>
            </div>
          </Link>
          <Link href={'/login/naver'}>
            <div className={styles.btn__naver}>
              <img src="/logo/naver.png" />
              <span>네이버 계정으로 로그인</span>
            </div>
          </Link>
          <Link href={'/login/google'}>
            <div className={styles.btn__google}>
              <img src="/logo/google.png" />
              <span>구글 계정으로 로그인</span>
            </div>
          </Link>
        </div>
      </section>
    </div>
  );
}
