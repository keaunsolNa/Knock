'use client';

import { apiRequest } from '@/utils/api';
import styles from './page.module.scss';
import { AnimatePresence, motion } from 'framer-motion';
import { useParams } from 'next/navigation';
import { useEffect, useState } from 'react';
import { useAppDispatch } from '@/redux/store';
import { alarmSettingList, alarmCategoryList, categoryToText, alarmToText } from '@/utils/typeToText';
import { setModal } from '@/redux/modalSlice';

export default function Page() {
  const dispatch = useAppDispatch();
  const { category } = useParams() as { category: string };
  const alarmIdx = alarmCategoryList.findIndex((val) => val === category);

  const [alarm, setAlarm] = useState<string[] | null>(null);
  const [isFirst, setIsFirst] = useState(true);

  const handleToggleClick = () => {
    setAlarm((prev) => {
      const newAlarm = [...prev];
      newAlarm[alarmIdx] = newAlarm[alarmIdx] === 'NONE' ? 'ZERO_DAY' : 'NONE';
      return newAlarm;
    });
  };

  const handleAlarmChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value;
    setAlarm((prev) => {
      const newAlarm = [...prev];
      newAlarm[alarmIdx] = newValue;
      return newAlarm;
    });
  };

  const getAlarmSetting = async () => {
    const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/getAlarmTimings`, dispatch, {
      method: 'GET',
    });

    if (response.status === 401) {
      dispatch(setModal({ isOpen: true }));
      return;
    }

    if (!response.ok) {
      throw new Error('유저 알림세팅 데이터 조회 API 에러');
    }

    const data: string[] = await response.json();
    setAlarm(data);
  };

  const setNewAlarmSetting = async () => {
    const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/${category}/changeAlarm`, dispatch, {
      method: 'POST',
      body: JSON.stringify(alarm),
    });

    if (!response.ok) {
      throw new Error('알림 변경 실패');
    }
  };

  useEffect(() => {
    if (!category) return;
    getAlarmSetting();
  }, [category]);

  useEffect(() => {
    if (alarm === null) return;

    if (isFirst) {
      setIsFirst(false);
    } else {
      setNewAlarmSetting();
    }
  }, [alarm]);

  return (
    <>
      {alarm && (
        <div className={styles.container}>
          <div className={styles.div__about}>
            회원님이 설정한 알림에 따라, 관심 있는 콘텐츠의 개봉/티켓 오픈일이 다가오면 알림을 보내드립니다. <br /> <br />
            원하는 날짜를 설정하고, 중요한 순간을 놓치지 마세요! <br /> 푸시(push) 알림은 9시에 전송됩니다.
          </div>

          <form className={styles.form__setting}>
            <div className={styles.div__toggle_wrapper}>
              <span>{`${categoryToText[category]} 알림`}</span>
              <motion.div
                className={styles.div__toggle_bar}
                onClick={handleToggleClick}
                style={{
                  justifyContent: alarm[alarmIdx] !== 'NONE' ? 'flex-end' : 'flex-start',
                  backgroundColor: alarm[alarmIdx] !== 'NONE' ? '#34c759' : '#d9d9da',
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
              {alarm[alarmIdx] !== 'NONE' && (
                <motion.div className={styles.div__radio_box} initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }}>
                  {alarmSettingList.map((setting) => (
                    <div key={`div__${setting}`}>
                      <input
                        className={styles.input__radio}
                        type="radio"
                        id={`setting__${setting}`}
                        value={setting}
                        checked={alarm[alarmIdx] === setting}
                        onChange={handleAlarmChange}
                      />
                      <label className={styles.label__radio} htmlFor={`setting__${setting}`}>
                        {alarmToText[setting]}
                      </label>
                    </div>
                  ))}
                </motion.div>
              )}
            </AnimatePresence>
          </form>
        </div>
      )}
    </>
  );
}
