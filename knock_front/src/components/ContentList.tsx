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
      {itemList.length > 0 ? (
        itemList.map((item) =>
          category === 'movie' ? (
            <MovieItem key={item.movieId} {...item} setAlarm={alarmList?.includes(item.movieId)} viewBtn={true} />
          ) : (
            <PerformItem key={item.id} {...item} genre={genre} setAlarm={alarmList?.includes(item.id)} viewBtn={true} />
          )
        )
      ) : (
        <div className={styles.div__no_item}>조회된 컨텐츠가 없습니다</div>
      )}
    </div>
  );
}
