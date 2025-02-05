import Image from 'next/image';
import Link from 'next/link';
import styles from './page.module.scss';

export default function Home() {
  return (
    <div className={styles.container}>
      <Image
        src={'/images/logo_potrait.png'}
        alt="Knock의 로고"
        width={250}
        height={250}
      />

      <Link href={'/movie'} className={styles.link} scroll={false}>
        영화
      </Link>
      <Link href={'/login'} className={styles.link} scroll={false}>
        로그인
      </Link>
      <Link href={'/mypage'} className={styles.link} scroll={false}>
        마이페이지
      </Link>
    </div>
  );
}
