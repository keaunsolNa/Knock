'use client';

import styles from '@/styles/components/header.module.scss';
import { FaArrowLeft, FaChevronDown, FaChevronUp } from 'react-icons/fa6';
import { FiMenu } from 'react-icons/fi';
import { usePathname } from 'next/navigation';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { useEffect, useState } from 'react';

export default function Header() {
  const pathName = usePathname().split('/').slice(1);
  const router = useRouter();
  const [foldMenu, setFoldMenu] = useState(true);

  const filterBtnBack = () => {
    if (pathName[0] === 'movie') {
      return !pathName[1];
    } else if (pathName[0] === 'performingArts') {
      return !pathName[2] || pathName[2] === 'submenu';
    } else if (pathName[0] === 'mypage') {
      return !pathName[1];
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
      case 'performingArts':
        if (!pathName[1]) return '공연 예술';
        if (pathName[1] === 'theater') return '연극';
        if (pathName[1] === 'musical') return '뮤지컬';
        if (pathName[1] === 'classical') return '클래식';
        if (pathName[1] === 'koreanTraditional') return '국악';
        if (pathName[1] === 'popularMusic') return '대중 음악';
        if (pathName[1] === 'westernKoreanDance') return '서양·한국무용';
        if (pathName[1] === 'popularDance') return '대중 무용';
        if (pathName[1] === 'circusMagic') return '서커스·마술';
        if (pathName[1] === 'complex') return '복합';
        if (pathName[1] === 'unknown') return '기타';
    }
  };

  const subMenuOnClick = () => {
    if (foldMenu) {
      router.push(`/performingArts/${pathName[1]}/submenu`);
      setFoldMenu(false);
    } else {
      router.push(`/performingArts/${pathName[1]}`);
      setFoldMenu(true);
    }
  };

  useEffect(() => {
    if (pathName[2] === 'submenu') {
      setFoldMenu(false);
    } else {
      setFoldMenu(true);
    }
  }, [pathName[2]]);

  return (
    <nav className={`${styles.container} ${pathName[0] === 'mypage' && styles.bg_color}`}>
      <div className={styles.prev} onClick={() => router.back()}>
        <FaArrowLeft className={filterBtnBack() && styles.hidden} />
      </div>

      <div className={styles.cur}>
        {pathName[0] === 'performingArts' && pathName[1] ? (
          <span className={styles.cur__wrapper} onClick={subMenuOnClick}>
            <span>{curMenu()}</span>
            {foldMenu ? <FaChevronDown /> : <FaChevronUp />}
          </span>
        ) : (
          <span>{curMenu()}</span>
        )}
      </div>

      <Link href={'/menu'}>
        <div className={styles.menu}>
          <FiMenu />
        </div>
      </Link>
    </nav>
  );
}
