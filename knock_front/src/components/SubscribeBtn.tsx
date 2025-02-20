'use client';

import { store, useAppDispatch } from '@/redux/store';
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
  const [subCnt, setSubCnt] = useState(favorites ? favorites.length : '0');
  const [isSub, setIsSub] = useState(false);

  let accessToken = store.getState().auth.accessToken;
  useEffect(() => {
    // const response = apiRequest(
    //   `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/movie/isSubscribe`,
    //   dispatch
    // );
  }, []);

  const onClickBtn = (e: React.MouseEvent<HTMLButtonElement>) => {
    // const response = apiRequest(
    //   `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/movie/sub`,
    //   dispatch,
    //   {
    //     method: 'POST',
    //     body: JSON.stringify({ movieId }),
    //   }
    // );

    const response = fetch(
      `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/movie/sub`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${accessToken}`,
        },
        body: JSON.stringify({ movieId }),
      }
    );
    console.log(response);

    setIsSub((prev) => !prev);
  };

  return (
    <>
      <button
        onClick={onClickBtn}
        className={
          isSub
            ? `${styles.btn__subscribe} ${styles.alarm__on}`
            : `${styles.btn__subscribe} ${styles.alarm__off}`
        }
      >
        <motion.div
          initial={{ scale: 1 }}
          animate={{ scale: isSub ? [1, 1.2, 1] : [1, 0.8, 1] }}
          transition={{ duration: 0.3 }}
        >
          {isSub ? <BsBellFill /> : <BsBell />}
        </motion.div>
        <span>{subCnt}</span>
      </button>
    </>
  );
}
