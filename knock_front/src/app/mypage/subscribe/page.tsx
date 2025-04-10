'use client';

import { ISubList } from '@/types';
import styles from './page.module.scss';
import { apiRequest } from '@/utils/api';
import { useAppDispatch } from '@/redux/store';
import { useEffect, useState } from 'react';
import { alarmCategoryList, categoryToText, genreToLink } from '@/utils/typeToText';
import MovieItem from '@/components/MovieItem';
import PerformItem from '@/components/PerformItem';
import { setModal } from '@/redux/modalSlice';

export default function Page() {
  const dispatch = useAppDispatch();

  const [subList, setSubList] = useState<ISubList>();
  const [category, setCategory] = useState('MOVIE');

  const getSubList = async () => {
    const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/getSubscribeList`, dispatch, {
      method: 'GET',
    });

    if (response.status === 401) {
      dispatch(setModal({ isOpen: true }));
    }

    if (!response.ok) {
      throw new Error('유저 구독 카테고리 조회 API 에러');
    }

    const data = await response.json();
    setSubList(data);
  };

  useEffect(() => {
    getSubList();
  }, []);

  return (
    <div className={styles.container}>
      <div className={styles.div__category_wrapper}>
        {alarmCategoryList.map((item) => {
          return (
            <div key={'btn_' + item} className={category === item ? styles.div__select_category : null} onClick={() => setCategory(item)}>
              {categoryToText[item]}
            </div>
          );
        })}
      </div>

      {!subList || subList[category].length === 0 ? (
        <div className={styles.div__no_item}>
          <h3>구독 목록이 없습니다</h3>
        </div>
      ) : (
        <div className={styles.div__item_wrapper}>
          <h5>총 {subList[category].length}개</h5>
          {subList[category].map((item) => {
            if (category === 'MOVIE') {
              return <MovieItem key={`${category}_${item.movieId}`} {...item} viewBtn={false} />;
            } else {
              return <PerformItem key={`${category}_${item.id}`} {...item} genre={genreToLink[item.categoryLevelTwo.nm]} viewBtn={false} />;
            }
          })}
        </div>
      )}
    </div>
  );
}
