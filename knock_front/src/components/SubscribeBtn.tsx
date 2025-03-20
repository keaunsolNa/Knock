'use client';

import { useAppDispatch } from '@/redux/store';
import styles from '@/styles/components/subscribe-btn.module.scss';
import { apiRequest } from '@/utils/api';
import { useEffect, useState } from 'react';
import { BsBell, BsBellFill } from 'react-icons/bs';
import { motion } from 'framer-motion';
import { BeatLoader } from 'react-spinners';
import { usePathname } from 'next/navigation';

interface IProps {
  favorites: number;
  id: string;
}

export default function SubscribeBtn({ favorites, id }: IProps) {
  const category = usePathname().split('/').slice(1)[0];
  const dispatch = useAppDispatch();
  const [sub, setSub] = useState<{ subCnt: Number; isSub: boolean }>(null);
  const [isLoading, setIsLoading] = useState(false);

  /**
   * 유저가 이 영화를 구독했는지 확인
   * : mount 시점에 1번
   */
  const checkSubscribe = async () => {
    const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/${category}/isSubscribe`, dispatch, {
      method: 'POST',
      body: JSON.stringify({ value: id }),
    });

    const isSub = await response.json();

    // 2. 구독여부와 구독 수 세팅
    setSub({ subCnt: favorites, isSub: isSub });
  };

  /**
   * Subscribe
   */
  const subScribeFetch = async () => {
    const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/${category}/sub`, dispatch, {
      method: 'POST',
      body: JSON.stringify({ value: id }),
    });
    const data = await response.json();

    if (data !== -1) {
      setSub({ subCnt: data, isSub: true });
    }

    setIsLoading(false);
  };

  /**
   * Subscribe Cancel
   */
  const subScribeCancelFetch = async () => {
    const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/${category}/cancelSub`, dispatch, {
      method: 'POST',
      body: JSON.stringify({ value: id }),
    });
    const data = await response.json();
    if (data !== -1) {
      setSub({ subCnt: data, isSub: false });
    }
    setIsLoading(false);
  };

  useEffect(() => {
    checkSubscribe();
  }, []);

  const onClickBtn = async (e: React.MouseEvent<HTMLButtonElement>) => {
    try {
      setIsLoading(true);

      if (sub.isSub) {
        await subScribeCancelFetch();
      } else {
        await subScribeFetch();
      }
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <>
      {sub &&
        (isLoading ? (
          <button className={styles.btn__loading} disabled>
            {' '}
            <BeatLoader size={10} color={'#ffffff'} />
          </button>
        ) : (
          <button
            onClick={onClickBtn}
            className={sub.isSub ? `${styles.btn__subscribe} ${styles.alarm__on}` : `${styles.btn__subscribe} ${styles.alarm__off}`}
          >
            <motion.div initial={{ scale: 1 }} animate={{ scale: sub.isSub ? [1, 1.2, 1] : [1, 0.8, 1] }} transition={{ duration: 0.3 }}>
              {sub.isSub ? <BsBellFill /> : <BsBell />}
            </motion.div>
            <span>{`${sub.subCnt}`}</span>
          </button>
        ))}
    </>
  );
}
