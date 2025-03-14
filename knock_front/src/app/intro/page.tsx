'use client';

import Link from 'next/link';
import styles from './page.module.scss';
import Carousel from '@/components/intro/Carousel';

export default function Page() {
  return (
    <div className={styles.container}>
      <Carousel />
      <section className={styles.section__link}>
        <Link href={'/login'}>
          <div className={styles.btn__login}>로그인하고 KNOCK 시작하기</div>
        </Link>
        <p>계정 생성 시 KNOCK의 개인정보 처리방침 및 이용약관에 동의하게 됩니다.</p>
      </section>
    </div>
  );
}
