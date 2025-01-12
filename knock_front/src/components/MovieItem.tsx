'use client';

import styles from '@/styles/components/movie-item.module.scss';
import Image from 'next/image';
import { useState } from 'react';
import { BsBell, BsBellFill } from 'react-icons/bs';

export default function MovieItem() {
  const [alarm, setAlarm] = useState(true);

  return (
    <div className={styles.container}>
      <div className={styles.img__container}>
        <Image src={`/images/89398_1000.jpg`} fill alt="영화포스터" />
      </div>
      <div className={styles.div__detail}>
        <div className={styles.div__info}>
          <p className={styles.p__date}>2025.01.24 개봉</p>
          <p className={styles.p__title}>검은 수녀들</p>
        </div>
        <div className={styles.div__btn}>
          <button
            onClick={() => setAlarm((prev) => !prev)}
            className={
              alarm
                ? `${styles.btn__alarm} ${styles.btn__alarm_set}`
                : styles.btn__alarm
            }
          >
            {alarm ? <BsBellFill /> : <BsBell />}
          </button>
        </div>
      </div>
    </div>
  );
}
