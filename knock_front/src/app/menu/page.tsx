'use client';

import Link from 'next/link';
import styles from './page.module.scss';
import { IoClose } from 'react-icons/io5';
import { FaChevronDown } from 'react-icons/fa6';
import { FaChevronUp } from 'react-icons/fa6';
import { useRouter } from 'next/navigation';
import { useState } from 'react';
import { AnimatePresence, motion } from 'framer-motion';

const smallMenuLinks = [
  { name: '연극', link: 'theater' },
  { name: '뮤지컬', link: 'musical' },
  { name: '서양음악(클래식)', link: 'classical' },
  { name: '한국음악(국악)', link: 'koreanTraditional' },
  { name: '대중음악', link: 'popularMusic' },
  { name: '무용(서양/한국무용)', link: 'westernKoreanDance' },
  { name: '대중무용', link: 'popularDance' },
  { name: '서커스/마술', link: 'circusMagic' },
  { name: '복합', link: 'complex' },
  { name: '기타', link: 'unknown' },
];

const itemVariants = {
  hidden: { opacity: 0, y: 20 },
  visible: (i: number) => ({
    opacity: 1,
    y: 0,
    transition: { delay: i * 0.1, duration: 0.2 },
  }),
  exit: { opacity: 0, y: -20, transition: { duration: 0.1 } },
};

const menuVariants = {
  open: {
    height: 'auto',
    opacity: 1,
    transition: { duration: 0.5, ease: 'easeInOut' },
  },
  closed: {
    height: 0,
    opacity: 0,
    transition: { duration: 0.3, ease: 'easeInOut' },
  },
};

export default function Page() {
  const [fold, setFold] = useState(true);
  const router = useRouter();

  const convertFold = (e: React.MouseEvent<SVGElement>) => {
    e.preventDefault();
    setFold((prev) => !prev);
  };

  return (
    <div className={styles.container}>
      <div className={styles.div__header}>
        <div className={styles.div__logo}>
          <span className={styles.span__point}>K</span>
          <span>NOCK</span>
        </div>
        <div className={styles.div__close} onClick={() => router.back()}>
          <IoClose />
        </div>
      </div>

      <nav className={styles.nav__container}>
        {/* 대메뉴 */}
        <li>
          <Link href={'/movie'}>
            <div className={styles.div__big_menu}>
              <span>영화</span>
            </div>
          </Link>
        </li>

        <li>
          <Link href={'/performingArts'}>
            <div className={styles.div__big_menu}>
              <span>공연 예술</span>
              {fold ? <FaChevronDown onClick={convertFold} /> : <FaChevronUp onClick={convertFold} />}
            </div>
          </Link>

          {/* 서브메뉴 */}
          <AnimatePresence>
            {!fold && (
              <motion.ul className={styles.ul__small_menu} initial="closed" animate="open" exit="closed" variants={menuVariants}>
                {smallMenuLinks.map(({ name, link }, idx) => {
                  return (
                    <motion.li
                      key={`performingArts_genre_${idx}`}
                      custom={idx}
                      variants={itemVariants}
                      initial="hidden"
                      animate="visible"
                      exit="exit"
                    >
                      <Link href={`/performingArts/${link}`}>
                        <div>{name}</div>
                      </Link>
                    </motion.li>
                  );
                })}
              </motion.ul>
            )}
          </AnimatePresence>
        </li>

        {/* 대메뉴 */}
        <li>
          <Link href={'/mypage'}>
            <div className={styles.div__big_menu}>
              <span>마이페이지</span>
            </div>
          </Link>
        </li>
      </nav>
    </div>
  );
}
