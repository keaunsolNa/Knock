'use client';

import styles from '@/styles/components/header.module.scss';
import { FaArrowLeft, FaChevronDown, FaChevronUp } from 'react-icons/fa6';
import { FiMenu } from 'react-icons/fi';
import { usePathname } from 'next/navigation';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { useEffect, useMemo, useState } from 'react';

export default function Header() {
  const pathName = usePathname();
  const router = useRouter();
  const [foldMenu, setFoldMenu] = useState(true);

  const path = useMemo(() => pathName.split('/').slice(1), [pathName]);
  const root = path[0];
  const sub = path[1];
  const third = path[2];

  /**
   * 뒤로가기 버튼 숨김 여부
   */
  const hideBackButton = useMemo(() => {
    if (root === 'movie') return !sub;
    if (root === 'performingArts') return !third || third === 'submenu';
    if (root === 'mypage') return !sub;
    return false;
  }, [root, sub, third]);

  /**
   * 현재 메뉴 텍스트 반환
   */
  const currentMenu = useMemo(() => {
    if (root === 'movie') return '영화';

    if (root === 'mypage') {
      const mypageMap: Record<string, string> = {
        '': '마이페이지',
        category: '카테고리 설정',
        subscribe: '구독 목록',
        alarm: '알람 설정',
      };
      return mypageMap[sub ?? ''] ?? '마이페이지';
    }

    if (root === 'performingArts') {
      const paMap: Record<string, string> = {
        '': '공연 예술',
        theater: '연극',
        musical: '뮤지컬',
        classical: '클래식',
        koreanTraditional: '국악',
        popularMusic: '대중 음악',
        westernKoreanDance: '서양·한국무용',
        popularDance: '대중 무용',
        circusMagic: '서커스·마술',
        complex: '복합',
        unknown: '기타',
      };
      return paMap[sub ?? ''] ?? '공연 예술';
    }

    return '';
  }, [root, sub]);

  const subMenuOnClick = () => {
    if (foldMenu) {
      router.push(`/performingArts/${sub}/submenu`);
    } else {
      router.back();
    }
    setFoldMenu(!foldMenu);
  };

  // submenu 상태 감지
  useEffect(() => {
    setFoldMenu(third !== 'submenu');
  }, [third]);

  return (
    <nav className={`${styles.container} ${root === 'mypage' && styles.bg_color}`}>
      <div className={styles.prev} onClick={() => router.back()}>
        <FaArrowLeft className={hideBackButton && styles.hidden} />
      </div>

      <div className={styles.cur}>
        {root === 'performingArts' && sub ? (
          <span className={styles.cur__wrapper} onClick={subMenuOnClick}>
            <span>{currentMenu}</span>
            {foldMenu ? <FaChevronDown /> : <FaChevronUp />}
          </span>
        ) : (
          <span>{currentMenu}</span>
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
