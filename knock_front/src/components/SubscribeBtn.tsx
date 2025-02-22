'use client';

import { useAppDispatch } from '@/redux/store';
import styles from '@/styles/components/subscribe-btn.module.scss';
import { IUser } from '@/types';
import { apiRequest } from '@/utils/api';
import { useEffect, useState } from 'react';
import { BsBell, BsBellFill } from 'react-icons/bs';
import { motion } from 'framer-motion';

interface IProps {
  favorites: IUser[];
  movieId: string;
}

export default function SubscribeBtn({ favorites, movieId }: IProps) {
  const dispatch = useAppDispatch();
  const [sub, setSub] = useState<{ subCnt: Number; isSub: boolean }>(null);
  const [isLoading, setIsLoading] = useState(false);

  /**
   * 유저가 이 영화를 구독했는지 확인
   * : mount 시점에 1번
   */
  const checkSubscribe = async () => {
    const response = await apiRequest(
      `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/movie/isSubscribe`,
      dispatch,
      {
        method: 'POST',
        body: JSON.stringify({ value: movieId }),
      }
    );

    const isSub = await response.json();

    // 2. 구독여부와 구독 수 세팅
    setSub({ subCnt: favorites ? favorites.length : 0, isSub: isSub });
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
        body: JSON.stringify({ value: movieId }),
      }
    );
    const data = await response.json();
    setSub({ subCnt: data, isSub: true });
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
        body: JSON.stringify({ value: movieId }),
      }
    );
    const data = await response.json();
    setSub({ subCnt: data, isSub: false });
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
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      {sub && (
        <button
          onClick={onClickBtn}
          disabled={isLoading}
          className={
            sub.isSub
              ? `${styles.btn__subscribe} ${styles.alarm__on}`
              : `${styles.btn__subscribe} ${styles.alarm__off}`
          }
        >
          <motion.div
            initial={{ scale: 1 }}
            animate={{ scale: sub.isSub ? [1, 1.2, 1] : [1, 0.8, 1] }}
            transition={{ duration: 0.3 }}
          >
            {sub.isSub ? <BsBellFill /> : <BsBell />}
          </motion.div>
          <span>{`${sub.subCnt}`}</span>
        </button>
      )}
    </>
  );
}
