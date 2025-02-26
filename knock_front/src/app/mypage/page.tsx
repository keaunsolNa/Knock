'use client';

import styles from './page.module.scss';
import MenuItem from '@/components/mypage/MenuItem';
import Profile from '@/components/mypage/Profile';
import { clearAccessToken } from '@/redux/authSlice';
import { useAppDispatch } from '@/redux/store';
import { IUser } from '@/types';
import { alarmToText, categoryToText, alarmCategoryList } from '@/utils/alarm';
import { apiRequest } from '@/utils/api';
import { notFound, useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';

export default function Page() {
  const dispatch = useAppDispatch();
  const router = useRouter();
  const [userData, setUserData] = useState<IUser>(null);

  const handleOnClickLogout = async () => {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/logout`,
      {
        method: 'DELETE',
        credentials: 'include',
      }
    );
    if (response.ok) {
      dispatch(clearAccessToken());
      router.push('/login');
    }
  };

  const getUserData = async () => {
    const response = await apiRequest(
      `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/getUserInfo`,
      dispatch,
      {
        method: 'GET',
      }
    );

    if (!response.ok) {
      notFound();
    }

    const data: IUser = await response.json();
    setUserData(data);
  };

  useEffect(() => {
    getUserData();
  }, []);

  return (
    <>
      {userData && (
        <div className={styles.container}>
          <Profile userData={userData} />

          <section className={styles.section__subscribe}>
            <h2>📌 나의 구독</h2>
            <div className={styles.div__menu_box}>
              <MenuItem
                name="카테고리"
                link="/mypage/category"
                value={
                  categoryToText[userData.favoriteLevelOne.toLocaleLowerCase()]
                }
              />
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

          {userData.loginType === 'GUEST' ? (
            <section className={styles.section__account}>
              <h2>🔗 계정 연동</h2>
              <p>
                데이터가 기기에만 저장되어 있습니다. 앱 삭제, 기기 변동, 예기치
                않은 오류 발생 시 데이터가 손실될 가능성이 있습니다.
                <br /> <br />
                간단하게 계정 연동을 하고, 보다 안전하게 데이터를 보호하세요!
              </p>
              <div className={styles.div__login_wrapper}>
                <button className={styles.btn} />
                <button className={styles.btn} />
                <button className={styles.btn} />
              </div>
            </section>
          ) : (
            <button
              className={styles.btn__logout}
              onClick={handleOnClickLogout}
            >
              로그아웃
            </button>
          )}
        </div>
      )}
    </>
  );
}
