'use client';

import styles from '@/styles/components/header.module.scss';
import { FaArrowLeft } from 'react-icons/fa6';
import { FaUserCircle } from 'react-icons/fa';
import { FiMenu } from 'react-icons/fi';
import { usePathname } from 'next/navigation';
import { useRouter } from 'next/navigation';
import Link from 'next/link';

export default function Header() {
  const pathName = usePathname().split('/').slice(1);
  const router = useRouter();

  const handleGoBack = () => {
    // Movie
    if (pathName[0] === 'movie') {
      if (!pathName[1] || pathName[1] === 'search') {
        router.push('/');
      } else {
        router.back();
      }
    } else {
      router.back();
    }
  };

  const curMenu = () => {
    switch (pathName[0]) {
      case 'movie':
        return '영화';
      case 'mypage':
        if (!pathName[1]) return '마이페이지';
        if (pathName[1] === 'category') return '카테고리 설정';
        if (pathName[1] === 'subscribe') return '구독 목록';
        if (pathName[1] === 'alarm') return '알람 설정';
    }
  };
  return (
    <nav
      className={`${styles.container} ${pathName[0] === 'mypage' && styles.bg_color}`}
    >
      <div className={styles.prev} onClick={handleGoBack}>
        <FaArrowLeft />
      </div>
      <div className={styles.cur}>
        <span>{curMenu()}</span>
      </div>
      <Link href={'/menu'}>
        <div className={styles.menu}>
          {pathName[0] !== 'mypage' ? <FaUserCircle /> : null}
          <FiMenu />
        </div>
      </Link>
    </nav>
  );
}
