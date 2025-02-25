'use client';

import { useAppDispatch } from '@/redux/store';
import styles from './page.module.scss';
import { apiRequest } from '@/utils/api';
import { notFound } from 'next/navigation';
import { useEffect } from 'react';

export default function Page() {
  const dispatch = useAppDispatch();

  const getSubCategory = async () => {
    const response = await apiRequest(
      `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/getSubCategory`,
      dispatch,
      {
        method: 'GET',
      }
    );
    console.log(response);

    if (!response.ok) {
      notFound();
    }

    const data = await response.text();
    console.log(data);
  };

  const setNewAlarmSetting = async () => {
    const response = await apiRequest(
      `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/changeCategory`,
      dispatch,
      {
        method: 'POST',
        body: JSON.stringify({ value: 'opera' }),
      }
    );

    if (!response.ok) {
      return <div>에러</div>;
    }

    const data = await response.json();
  };

  useEffect(() => {
    getSubCategory();
  }, []);

  return (
    <div className={styles.container}>
      <div className={styles.div__about}>
        회원님이 설정한 카테고리가 메인 화면에 노출됩니다.
        <br />
        선호 카테고리를 설정해주세요.
      </div>
      <form>
        <div className={styles.div__radio_box}>
          <div>
            <input type="radio" id="movie" name="category" value="movie" />
            <label htmlFor="movie">영화</label>
          </div>
          <div>
            <input type="radio" id="musical" name="category" value="musical" />
            <label htmlFor="musical">뮤지컬</label>
          </div>
          <div>
            <input
              type="radio"
              id="opera"
              name="category"
              value="opera"
              onClick={setNewAlarmSetting}
            />
            <label htmlFor="opera">오페라</label>
          </div>
          <div>
            <input
              type="radio"
              id="exhibition"
              name="category"
              value="exhibition"
            />
            <label htmlFor="exhibition">전시회</label>
          </div>
        </div>
      </form>
    </div>
  );
}
