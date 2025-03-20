'use client';

import styles from '@/styles/components/content-item.module.scss';
import Image from 'next/image';
import { useEffect, useState } from 'react';
import { BsBell, BsBellFill } from 'react-icons/bs';
import { motion } from 'framer-motion';
import { IPerformingArts } from '@/types';
import Link from 'next/link';
import { BeatLoader } from 'react-spinners';
import { apiRequest } from '@/utils/api';
import { useAppDispatch } from '@/redux/store';

interface IProps extends IPerformingArts {
  setAlarm?: boolean;
  viewBtn?: boolean;
  genre: string;
}

export default function PerformItem(props: IProps) {
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
    const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/performingArts/sub`, dispatch, {
      method: 'POST',
      body: JSON.stringify({ value: props.id }),
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
    const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/performingArts/cancelSub`, dispatch, {
      method: 'POST',
      body: JSON.stringify({ value: props.id }),
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

  const dateFormat = (date: Date) => {
    date = new Date(date);

    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');

    return `${year}.${month}.${day}`;
  };

  return (
    <Link rel="prefetch" href={`/performingArts/${props.genre}/${props.id}`}>
      <div className={styles.container}>
        <div className={styles.img__wrapper}>
          <Image
            src={props.poster ?? '/images/noImage.png'}
            fill
            alt={props.poster ? `${props.name} 포스터` : `${props.name} 포스터 대체 이미지`}
            sizes="100%"
          />
        </div>

        <div className={styles.div__info}>
          <div>
            <p className={styles.p__date}>
              {props.from === props.to ? `${dateFormat(props.from)}` : `${dateFormat(props.from)} ~ ${dateFormat(props.to)}`}
            </p>
            <div className={styles.div__category_list}>
              <div key={`${props.id}_area`}>{props.area}</div>
              <div key={`${props.id}_state`}>{props.prfState}</div>
            </div>
          </div>
          <p className={styles.p__title}>{props.name}</p>
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
