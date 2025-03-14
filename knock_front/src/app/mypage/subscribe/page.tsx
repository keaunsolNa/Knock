'use client';

import { ISubList } from '@/types';
import styles from './page.module.scss';
import { apiRequest } from '@/utils/api';
import { useAppDispatch } from '@/redux/store';
import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { alarmCategoryList, categoryToText } from '@/utils/alarm';
import ContentItem from '@/components/ContentItem';

export default function Page() {
  const dispatch = useAppDispatch();
  const router = useRouter();

  const [subList, setSubList] = useState<ISubList>();
  const [category, setCategory] = useState('MOVIE');

  const getSubList = async () => {
    const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/getSubscribeList`, dispatch, {
      method: 'GET',
    });

    if (!response.ok) {
      if (response.status === 401) {
        router.push('/login');
      }

      return <div>서버 에러</div>;
    }
    const data = await response.json();
    setSubList(data);
  };

  useEffect(() => {
    getSubList();
  }, []);

  return (
    subList && (
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

        {subList[category] === null || subList[category].length === 0 ? (
          <div className={styles.div__no_item}>
            <h3>구독 목록이 없습니다</h3>
          </div>
        ) : (
          <div className={styles.div__item_wrapper}>
            <h5>총 {subList[category].length}개</h5>
            {subList[category].map((item) => (
              <ContentItem key={`${category}_${item.movieId}`} {...item} viewBtn={false} />
            ))}
          </div>
        )}
      </div>
    )
  );
}
