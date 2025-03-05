'use client';

import styles from '@/styles/components/content-item.module.scss';
import Image from 'next/image';
import { useEffect, useState } from 'react';
import { BsBell, BsBellFill } from 'react-icons/bs';
import { motion } from 'framer-motion';
import { IMovie } from '@/types';
import Link from 'next/link';
import { BeatLoader } from 'react-spinners';
import { apiRequest } from '@/utils/api';
import { useAppDispatch } from '@/redux/store';
import CategoryItem from './CategoryItem';

interface IProps extends IMovie {
  setAlarm?: boolean;
  viewBtn?: boolean;
}

export default function ContentItem({ viewBtn = true, ...props }: IProps) {
  const dispatch = useAppDispatch();
  const [alarm, setAlarm] = useState<undefined | boolean>(props.setAlarm);

  useEffect(() => {
    setAlarm(props.setAlarm);
  }, [props]);

  const handleAlarmClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    e.preventDefault();

    if (alarm) {
      subScribeCancelFetch();
    } else {
      subScribeFetch();
    }
  };

  /**
   * Subscribe
   */
  const subScribeFetch = async () => {
    const response = await apiRequest(
      `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/movie/sub`,
      dispatch,
      {
        method: 'POST',
        body: JSON.stringify({ value: props.movieId }),
      }
    );
    if (response.ok) setAlarm(true);
  };

  /**
   * Subscribe Cancel
   */
  const subScribeCancelFetch = async () => {
    const response = await apiRequest(
      `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/movie/cancelSub`,
      dispatch,
      {
        method: 'POST',
        body: JSON.stringify({ value: props.movieId }),
      }
    );
    if (response.ok) setAlarm(false);
  };

  return (
    <Link rel="prefetch" href={`/movie/${props.movieId}`}>
      <div className={styles.container}>
        <div className={styles.img__wrapper}>
          {props.posterBase64 ? (
            <Image
              src={props.posterBase64}
              fill
              alt="영화포스터"
              sizes="100%"
            />
          ) : (
            <div className={styles.div__no_img}>no img</div>
          )}
        </div>

        <div className={styles.div__info}>
          <div>
            <p className={styles.p__date}>
              {props.openingTime === '개봉 예정'
                ? '미정'
                : `${props.openingTime} 개봉`}
            </p>
            <div className={styles.div__category_list}>
              {props.categoryLevelTwo.slice(0, 2).map((category) => (
                <div key={`${props.movieId}_${category.id}`}>{category.nm}</div>
              ))}
            </div>
          </div>
          <p className={styles.p__title}>{props.movieNm}</p>
        </div>

        {viewBtn && (
          <div className={styles.div__alarm}>
            {alarm === undefined ? (
              <div className={styles.div__alarm_loading}>
                <BeatLoader size={10} color={'#ffffff'} />
              </div>
            ) : (
              <button
                onClick={handleAlarmClick}
                className={alarm ? styles.btn__alarm_on : styles.btn__alarm_off}
              >
                <motion.div
                  initial={{ scale: 1 }}
                  animate={{ scale: alarm ? [1, 1.2, 1] : [1, 0.8, 1] }}
                  transition={{ duration: 0.3 }}
                >
                  {alarm ? <BsBellFill /> : <BsBell />}
                </motion.div>
              </button>
            )}
          </div>
        )}
      </div>
    </Link>
  );
}
