'use client';

import styles from '@/styles/components/movie-item.module.scss';
import Image from 'next/image';
import { useState } from 'react';
import { BsBell, BsBellFill } from 'react-icons/bs';
import { motion } from 'framer-motion';
import { IMovie } from '@/types';

interface IProps extends IMovie {
  display: boolean;
}

export default function MovieItem(props: IProps) {
  const [alarm, setAlarm] = useState(false);

  return (
    <div className={props.display ? styles.container : styles.container_none}>
      <div className={styles.img__container}>
        <Image src={props.posterBase64} fill alt="영화포스터" sizes="100%" />
      </div>
      <div className={styles.div__detail}>
        <div className={styles.div__info}>
          <p className={styles.p__date}>{props.openingTime} 개봉</p>
          <p className={styles.p__title}>{props.movieNm}</p>
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
            <motion.div
              initial={{ scale: 1 }}
              animate={{ scale: alarm ? [1, 1.2, 1] : [1, 0.8, 1] }}
              transition={{ duration: 0.3 }}
              style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
              }}
            >
              {alarm ? (
                <BsBellFill color={alarm ? '#ff6347' : '#ccc'} />
              ) : (
                <BsBell />
              )}
            </motion.div>
          </button>
        </div>
      </div>
    </div>
  );
}
