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
  { name: '영화', link: '/movie' },
  { name: '공연 예술', link: '/performingArts' },
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
  const [fold, setFold] = useState(false);
  const router = useRouter();

  const convertFold = () => setFold((prev) => !prev);

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
          <div className={styles.div__big_menu}>
            <span>카테고리</span>
            {fold ? <FaChevronDown onClick={convertFold} /> : <FaChevronUp onClick={convertFold} />}
          </div>

          {/* 서브메뉴 */}
          <AnimatePresence>
            {!fold && (
              <motion.ul className={styles.ul__small_menu} initial="closed" animate="open" exit="closed" variants={menuVariants}>
                {smallMenuLinks.map(({ name, link }, idx) => {
                  return (
                    <motion.li key={`category_${idx}`} custom={idx} variants={itemVariants} initial="hidden" animate="visible" exit="exit">
                      <Link href={link}>
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
