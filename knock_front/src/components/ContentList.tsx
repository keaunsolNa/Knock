'use client';

import { IMovie } from '@/types';
import MovieItem from './MovieItem';
import styles from '@/styles/components/list-box.module.scss';
import { apiRequest } from '@/utils/api';
import { useAppDispatch } from '@/redux/store';
import { useEffect, useState } from 'react';

export default function ContentList({
  itemList,
  category,
}: {
  itemList: IMovie[];
  category: string;
}) {
  const dispatch = useAppDispatch();
  const [alarmList, setAlarmList] = useState<null | string[]>(null);

  const getSubscribeList = async () => {
    const response = await apiRequest(
      `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/${category}/getSubscribeList`,
      dispatch,
      {
        method: 'GET',
      }
    );

    if (!response.ok) return;
    const data = await response.json();
    setAlarmList(data);
  };

  useEffect(() => {
    getSubscribeList();
  }, []);

  return (
    <div className={styles.container}>
      {alarmList
        ? itemList.map((movie) => {
            if (alarmList.length > 0) {
              return (
                <MovieItem
                  key={movie.movieId}
                  {...movie}
                  setAlarm={alarmList.includes(movie.movieId)}
                />
              );
            } else {
              return (
                <MovieItem key={movie.movieId} {...movie} setAlarm={false} />
              );
            }
          })
        : itemList.map((movie) => <MovieItem key={movie.movieId} {...movie} />)}
    </div>
  );
}
