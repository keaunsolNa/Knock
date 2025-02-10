'use client';

import styles from './page.module.scss';
import { AnimatePresence, motion } from 'framer-motion';
import { useParams } from 'next/navigation';
import { useState } from 'react';

export default function Page() {
  const { category } = useParams();
  const [alarm, setAlarm] = useState(true);

  const alarmType = () => {
    switch (category) {
      case 'movie':
        return '영화 알림';
      case 'musical':
        return '뮤지컬 알림';
      case 'opera':
        return '오페라 알림';
      case 'exhibition':
        return '전시회 알림';
    }
  };
  const alarmSettingList = [
    { value: 1, text: '1시간 전' },
    { value: 2, text: '3시간 전' },
    { value: 3, text: '6시간 전' },
    { value: 4, text: '12시간 전' },
    { value: 5, text: '1일 전' },
    { value: 6, text: '3일 전' },
    { value: 7, text: '7일 전' },
  ];

  return (
    <div className={styles.container}>
      <div className={styles.div__about}>
        회원님이 설정한 시간에 따라, 관심 있는 콘텐츠의 개봉/티켓 오픈일이
        다가오면 알림을 보내드립니다. <br /> <br />
        원하는 시간을 설정하고, 중요한 순간을 놓치지 마세요!
      </div>

      <form className={styles.form__setting}>
        <div className={styles.div__toggle_wrapper}>
          <span>{alarmType()}</span>
          <motion.div
            className={styles.div__toggle_bar}
            onClick={() => setAlarm((prev) => !prev)}
            style={{
              justifyContent: alarm ? 'flex-end' : 'flex-start',
              backgroundColor: alarm ? '#34c759' : '#d9d9da',
            }}
          >
            <motion.div
              className={styles.div__toggle_circle}
              layout
              transition={{
                type: 'spring',
                visualDuration: 0.2,
                bounce: 0.2,
              }}
            />
          </motion.div>
        </div>

        <AnimatePresence>
          {alarm && (
            <motion.div
              className={styles.div__radio_box}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
            >
              {alarmSettingList.map((type) => (
                <div key={'setting' + type.value}>
                  <input
                    className={styles.input__radio}
                    id={'setting' + type.value}
                    type="radio"
                    value={type.value}
                    name="alarmSetting"
                  />
                  <label
                    className={styles.label__radio}
                    htmlFor={'setting' + type.value}
                  >
                    {type.text}
                  </label>
                </div>
              ))}
            </motion.div>
          )}
        </AnimatePresence>
      </form>
    </div>
  );
}
