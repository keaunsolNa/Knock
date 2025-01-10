'use client';

import styles from '@/styles/components/header.module.scss';
import { FaArrowLeft } from 'react-icons/fa6';
import { FaUserCircle } from 'react-icons/fa';
import { FiMenu } from 'react-icons/fi';
import { useRouter } from 'next/navigation';

export default function Header() {
  const router = useRouter();

  const handleGoBack = () => {
    if (document.referrer === '') {
      router.push('/');
    } else {
      router.back();
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
