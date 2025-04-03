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

interface IMovieProps extends IMovie {
  setAlarm?: boolean;
  viewBtn?: boolean;
}

export default function MovieItem(props: IMovieProps) {
  const dispatch = useAppDispatch();
  const [alarm, setAlarm] = useState<undefined | boolean>(props.setAlarm);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    setAlarm(props.setAlarm);
  }, [props]);

  const handleAlarmClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    e.preventDefault();

    setIsLoading(true);

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
    const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/movie/sub`, dispatch, {
      method: 'POST',
      body: JSON.stringify({ value: props.movieId }),
    });

    if (response.ok) {
      const data = await response.json();
      // TODO : fetching 실패시 에러 alert
      if (data !== -1) {
        setAlarm(true);
      }
    }

    setIsLoading(false);
  };

  /**
   * Subscribe Cancel
   */
  const subScribeCancelFetch = async () => {
    const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/movie/cancelSub`, dispatch, {
      method: 'POST',
      body: JSON.stringify({ value: props.movieId }),
    });

    if (response.ok) {
      const data = await response.json();
      // TODO : fetching 실패시 에러 alert
      if (data !== -1) {
        setAlarm(false);
      }
    }

    setIsLoading(false);
  };

  const posterImg = () => {
    if (!props.posterBase64) {
      if (props.img) {
        return `data:image/png;base64,${props.img}`;
      } else {
        return '/images/noImage.png';
      }
    } else {
      return props.posterBase64;
    }
  };

  return (
    <Link rel="prefetch" href={`/movie/${props.movieId}`}>
      <div className={styles.container}>
        <div className={styles.img__wrapper}>
          <Image
            src={posterImg()}
            fill
            alt={props.posterBase64 || props.img ? `${props.movieNm} 포스터` : `${props.movieNm} 포스터 대체 이미지`}
            sizes="100%"
          />
        </div>

        <div className={styles.div__info}>
          <div>
            <p className={styles.p__date}>{`${props.openingTime}${props.openingTime !== '개봉 예정' ? ' 개봉' : ''}`}</p>
            <div className={styles.div__category_list}>
              {props.categoryLevelTwo &&
                props.categoryLevelTwo.slice(0, 2).map((category) => <div key={`${props.movieId}_${category.id}`}>{category.nm}</div>)}
            </div>
          </div>
          <p className={styles.p__title}>{props.movieNm}</p>
        </div>

        {props.viewBtn && (
          <div className={styles.div__alarm}>
            {alarm === undefined ? (
              <div className={styles.div__alarm_loading}>
                <BeatLoader size={10} color={'#ffffff'} />
              </div>
            ) : (
              <button onClick={handleAlarmClick} className={alarm ? styles.btn__alarm_on : styles.btn__alarm_off}>
                <motion.div initial={{ scale: 1 }} animate={{ scale: alarm ? [1, 1.2, 1] : [1, 0.8, 1] }} transition={{ duration: 0.3 }}>
                  {isLoading ? <BeatLoader size={10} color={'#ffffff'} /> : alarm ? <BsBellFill /> : <BsBell />}
                </motion.div>
              </button>
            )}
          </div>
        )}
      </div>
    </Link>
  );
}
