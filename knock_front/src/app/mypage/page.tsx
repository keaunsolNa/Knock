'use client';

import styles from './page.module.scss';
import MenuItem from '@/components/mypage/MenuItem';
import Profile from '@/components/mypage/Profile';
import { useAppDispatch } from '@/redux/store';
import { IUser } from '@/types';
import { apiRequest } from '@/utils/api';
import { notFound } from 'next/navigation';
import { useEffect, useState } from 'react';

export default function Page() {
  const dispatch = useAppDispatch();
  const [userData, setUserData] = useState<IUser>(null);

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

  const categorySub = () => {
    switch (userData.favoriteLevelOne) {
      case 'MOVIE':
        return '영화';
      case 'MUSICAL':
        return '뮤지컬';
      case 'OPERA':
        return '오페라';
      case 'EXHIBITION':
        return '전시회';
    }
  };

  const alarmToText = {
    ZERO_HOUR: '설정안함',
    ONE_HOUR: '1시간 전',
    THR_HOUR: '3시간 전',
    SIX_HOUR: '6시간 전',
    TWE_HOUR: '12시간 전',
    ONE_DAY: '1일 전',
    THR_DAY: '3일 전',
    SEV_DAY: '7일 전',
  };

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
                value={categorySub()}
              />
              <MenuItem name="구독 목록" link="/mypage/subscribe" />
            </div>
          </section>

          <section className={styles.section__alarm}>
            <h2>⏰ 알림 설정</h2>
            <div className={styles.div__menu_box}>
              <MenuItem
                name="영화"
                link="/mypage/alarm/movie"
                value={alarmToText[userData.alarmTimings[0]]}
              />
              <MenuItem
                name="뮤지컬"
                link="/mypage/alarm/musical"
                value={alarmToText[userData.alarmTimings[1]]}
              />
              <MenuItem
                name="오페라"
                link="/mypage/alarm/opera"
                value={alarmToText[userData.alarmTimings[2]]}
              />
              <MenuItem
                name="전시회"
                link="/mypage/alarm/exhibition"
                value={alarmToText[userData.alarmTimings[3]]}
              />
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
            <button className={styles.btn__logout}>로그아웃</button>
          )}
        </div>
      )}
    </>
  );
}
