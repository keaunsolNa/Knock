'use client';

import { useAppDispatch } from '@/redux/store';
import styles from './page.module.scss';
import { apiRequest } from '@/utils/api';
import { notFound } from 'next/navigation';
import { useEffect, useState } from 'react';
import { alarmCategoryList, categoryToText } from '@/utils/typeToText';
import { setModal } from '@/redux/modalSlice';

export default function Page() {
  const dispatch = useAppDispatch();
  const [category, setCategory] = useState<string>();
  const [isFirst, setIsFirst] = useState(true);

  const handelChangeCategory = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCategory(e.target.value);
  };

  const getSubCategory = async () => {
    const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/getSubCategory`, dispatch, {
      method: 'GET',
    });

    if (response.status === 401) {
      console.log('401에러 발생');
      dispatch(setModal({ isOpen: true }));
    }

    if (!response.ok) {
      throw new Error('유저 구독 카테고리 조회 API 에러');
    }

    const data = await response.text();
    setCategory(data);
  };

  const setNewAlarmSetting = async () => {
    const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/changeCategory`, dispatch, {
      method: 'POST',
      body: JSON.stringify({ value: category }),
    });

    if (!response.ok) {
      throw new Error('유저 구독 카테고리 변경 API 에러');
    }
  };

  useEffect(() => {
    getSubCategory();
  }, []);

  useEffect(() => {
    if (!category) return;
    if (isFirst) {
      setIsFirst(false);
    } else {
      setNewAlarmSetting();
    }
  }, [category]);

  return (
    <div className={styles.container}>
      <div className={styles.div__about}>
        회원님이 설정한 카테고리가 메인 화면에 노출됩니다.
        <br />
        선호 카테고리를 설정해주세요.
      </div>
      <form>
        <div className={styles.div__radio_box}>
          {alarmCategoryList.map((setting) => {
            return (
              <div key={`div__${setting}`}>
                <input type="radio" id={setting} value={setting} checked={category === setting} onChange={handelChangeCategory} />
                <label htmlFor={setting}>{categoryToText[setting]}</label>
              </div>
            );
          })}
        </div>
      </form>
    </div>
  );
}
