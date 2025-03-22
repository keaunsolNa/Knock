'use client';

import { IMovie, IPerformingArts } from '@/types';
import styles from '@/styles/components/content-list.module.scss';
import { apiRequest } from '@/utils/api';
import { useAppDispatch } from '@/redux/store';
import { useEffect, useState } from 'react';
import MovieItem from './MovieItem';
import PerformItem from './PerformItem';
import { setModal } from '@/redux/modalSlice';

export default function ContentList({
  itemList,
  category,
  genre,
}: {
  itemList: IMovie[] | IPerformingArts[];
  category: string;
  genre?: string;
}) {
  const dispatch = useAppDispatch();
  const [alarmList, setAlarmList] = useState<null | string[]>(null);

  const getMovieSubscribeList = async () => {
    const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/movie/getSubscribeList`, dispatch, {
      method: 'GET',
    });

    if (response.status === 401) {
      console.log('401에러 발생');
      dispatch(setModal({ isOpen: true }));
    }

    if (!response.ok) {
      throw new Error('영화 구독목록 조회 API 에러');
    }

    const data = await response.json();

    setAlarmList(data);
  };
  const getPerformSubscribeList = async () => {
    const response = await apiRequest(
      `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/performingArts/${genre}/getSubscribeList`,
      dispatch,
      {
        method: 'GET',
      }
    );

    if (response.status === 401) {
      console.log('401에러 발생');
      dispatch(setModal({ isOpen: true }));
    }

    if (!response.ok) {
      throw new Error('공연예술 구독목록 조회 API 에러');
    }

    const data = await response.json();
    setAlarmList(data);
  };

  useEffect(() => {
    try {
      if (category === 'movie') {
        getMovieSubscribeList();
      } else if (category === 'performingArts') {
        getPerformSubscribeList();
      }
    } catch (error) {
      console.log(error);
    }
  }, []);

  return (
    <div className={styles.container}>
      {category === 'movie'
        ? alarmList
          ? itemList.map((movie) => {
              if (alarmList.length > 0) {
                return <MovieItem key={movie.movieId} {...movie} setAlarm={alarmList.includes(movie.movieId)} viewBtn={true} />;
              } else {
                return <MovieItem key={movie.movieId} {...movie} setAlarm={false} viewBtn={true} />;
              }
            })
          : itemList.map((movie) => <MovieItem key={movie.movieId} {...movie} viewBtn={true} />)
        : alarmList
          ? itemList.map((perform) => {
              if (alarmList.length > 0) {
                return <PerformItem key={perform.id} {...perform} genre={genre} setAlarm={alarmList.includes(perform.id)} viewBtn={true} />;
              } else {
                return <PerformItem key={perform.id} {...perform} genre={genre} setAlarm={false} viewBtn={true} />;
              }
            })
          : itemList.map((perform) => <PerformItem key={perform.id} {...perform} genre={genre} viewBtn={true} />)}
    </div>
  );
}
