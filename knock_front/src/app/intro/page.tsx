'use client';

import styles from './page.module.scss';
import { useRouter } from 'next/navigation';
import { PanInfo, motion, useAnimation } from 'framer-motion';
import { useEffect, useState } from 'react';

export default function Page() {
  const router = useRouter();
  const controls = useAnimation();

  const images = ['/images/howto_1.png', '/images/howto_2.png', '/images/howto_3.png'];
  const [imgIdx, setImgIdx] = useState(0);

  useEffect(() => {
    controls.start({ x: `-${imgIdx * 33}%` });
  }, [imgIdx, controls]);

  /**
   *  사용자의 푸시 알림 권한 요청
   */
  const handleAllowNotification = async () => {
    const permission = await Notification.requestPermission();

    if (permission !== 'granted') {
      alert('개봉 알림을 받기 위해서는 권한을 허용해야 합니다.');
    }
  };

  /**
   *  로그인 버튼 onClick 이벤트
   */
  const onClickHandler = async () => {
    await handleAllowNotification();
    router.push('/login');
  };

  /**
   *  이미지 드래그 이벤트
   */
  const handleDragEnd = (_, info: PanInfo) => {
    const offset = info.offset.x; // 드래그한 거리
    const direction = offset > 100 ? -1 : offset < -100 ? 1 : 0; // 일정 거리 이상이면 이동

    if (direction !== 0) {
      if ((imgIdx === 2 && direction === 1) || (imgIdx === 0 && direction === -1)) {
        // 끝에 도달했으면 원래 위치로 복귀
        controls.start({ x: `-${imgIdx * 33}%` });
      } else {
        setImgIdx((prev) => prev + direction);
      }
    } else {
      // 드래그 범위가 부족하면 원래 위치로 복귀
      controls.start({ x: `-${imgIdx * 33}%` });
    }
  };

  return (
    <div className={styles.container}>
      <section className={styles.section__carousel}>
        <motion.div
          className={styles.div__img_container}
          initial={{ x: `-${imgIdx * 33}%` }}
          animate={controls}
          transition={{ type: 'spring', stiffness: 500, damping: 30 }}
          drag="x"
          onDragEnd={handleDragEnd}
        >
          {images.map((imgSrc, idx) => (
            <img key={`howto_img_${idx}`} src={imgSrc} alt="Knock 사용방법" />
          ))}
        </motion.div>

        <div className={styles.div__dot_container}>
          {images.map((_, idx) => (
            <div key={`howto_dot_${idx}`} className={idx === imgIdx ? styles.div__cur_dot : null} onClick={() => setImgIdx(idx)}></div>
          ))}
        </div>
      </section>

      <section className={styles.section__link}>
        <div className={styles.btn__login} onClick={onClickHandler}>
          로그인하고 KNOCK 시작하기
        </div>

        <p>계정 생성 시 KNOCK의 개인정보 처리방침 및 이용약관에 동의하게 됩니다.</p>
      </section>
    </div>
  );
}
