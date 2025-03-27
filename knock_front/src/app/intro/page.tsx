'use client';

import styles from './page.module.scss';
import Carousel from '@/components/intro/Carousel';
import { useRouter } from 'next/navigation';

export default function Page() {
  const router = useRouter();

  // 사용자의 푸시 알림 권한 요청
  const handleAllowNotification = async () => {
    const permission = await Notification.requestPermission();

    if (permission !== 'granted') {
      alert('개봉 알림을 받기 위해서는 권한을 허용해야 합니다.');
    }
  };

  const onClickHandler = async () => {
    await handleAllowNotification();
    router.push('/login');
  };

  return (
    <div className={styles.container}>
      <Carousel />
      <section className={styles.section__link}>
        <div className={styles.btn__login} onClick={onClickHandler}>
          로그인하고 KNOCK 시작하기
        </div>

        <p>계정 생성 시 KNOCK의 개인정보 처리방침 및 이용약관에 동의하게 됩니다.</p>
      </section>
    </div>
  );
}
