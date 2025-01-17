'use client';

import styles from '@/styles/components/header.module.scss';
import { FaArrowLeft } from 'react-icons/fa6';
import { FaUserCircle } from 'react-icons/fa';
import { FiMenu } from 'react-icons/fi';
import { usePathname } from 'next/navigation';
import { useRouter } from 'next/navigation';

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
    }
  };

  return (
    <nav className={styles.container}>
      <div className={styles.prev} onClick={handleGoBack}>
        <FaArrowLeft />
      </div>
      <div className={styles.cur}>
        <span>영화</span>
      </div>
      <div className={styles.menu}>
        <FaUserCircle />
        <FiMenu />
      </div>
    </nav>
  );
}
