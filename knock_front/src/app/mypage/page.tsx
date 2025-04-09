'use client';

import styles from './page.module.scss';
import MenuItem from '@/components/mypage/MenuItem';
import Profile from '@/components/mypage/Profile';
import { clearAuth } from '@/redux/authSlice';
import { useAppDispatch } from '@/redux/store';
import { IUser } from '@/types';
import { alarmToText, categoryToText, alarmCategoryList } from '@/utils/typeToText';
import { apiRequest } from '@/utils/api';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import { setModal } from '@/redux/modalSlice';

export default function Page() {
  const dispatch = useAppDispatch();
  const router = useRouter();
  const [userData, setUserData] = useState<IUser>(null);

  const handleOnClickLogout = async () => {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/logout`, {
      method: 'POST',
      credentials: 'include',
    });
    if (response.ok) {
      dispatch(clearAuth());
      router.push('/login');
    }
  };

  const getUserData = async () => {
    const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/getUserInfo`, dispatch, {
      method: 'GET',
    });

    if (response.status === 401) {
      console.log('401에러 발생');
      dispatch(setModal({ isOpen: true }));
    }

    if (!response.ok) {
      throw new Error('유저 데이터 조회 API 에러');
    }

    const data: IUser = await response.json();
    setUserData(data);
  };

  useEffect(() => {
    getUserData();
  }, []);

  if (!userData) return null;

  return (
    <div className={styles.container}>
      <Profile userData={userData} />

      <section className={styles.section__subscribe}>
        <h2>📌 나의 구독</h2>
        <div className={styles.div__menu_box}>
          <MenuItem name="카테고리" link="/mypage/category" value={categoryToText[userData.favoriteLevelOne.toLocaleLowerCase()]} />
          <MenuItem name="구독 목록" link="/mypage/subscribe" />
        </div>
      </section>

      <section className={styles.section__alarm}>
        <h2>⏰ 알림 설정</h2>
        <div className={styles.div__menu_box}>
          {alarmCategoryList.map((category, idx) => (
            <MenuItem
              key={`${category}_link`}
              name={categoryToText[category]}
              link={`/mypage/alarm/${category}`}
              value={alarmToText[userData.alarmTimings[idx]]}
            />
          ))}
        </div>
      </section>

      <button className={styles.btn__logout} onClick={handleOnClickLogout}>
        로그아웃
      </button>
    </div>
  );
}
