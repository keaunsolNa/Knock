'use client';

import { refreshAccessToken } from '@/redux/authSlice';
import { RootState, useAppDispatch } from '@/redux/store';
import { useEffect } from 'react';
import { useSelector } from 'react-redux';
import styles from './page.module.scss';
import { FadeLoader } from 'react-spinners';
import { useRouter } from 'next/navigation';

export default function SplashScreen() {
  const router = useRouter();
  const dispatch = useAppDispatch();
  const { isLoading, accessToken, redirectUrl } = useSelector((state: RootState) => state.auth);

  useEffect(() => {
    const checkAuth = async () => {
      await dispatch(refreshAccessToken());
    };
    checkAuth();
  }, []);

  useEffect(() => {
    if (!isLoading) {
      setTimeout(() => {
        router.push(accessToken ? redirectUrl : '/intro');
      }, 2000);
    }
  }, [isLoading]);

  return (
    <>
      <div className={styles.container}>
        <img src="/logo/knock.jpg" alt="App Icon" />
      </div>
      <div className={styles.loading}>
        <FadeLoader color="#f45f41" radius={2} />
      </div>
    </>
  );
}
