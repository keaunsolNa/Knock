'use client';

import { apiRequest } from '@/utils/api';
import styles from './page.module.scss';
import { AnimatePresence, motion } from 'framer-motion';
import { notFound, useParams } from 'next/navigation';
import { useEffect, useState } from 'react';
import { useAppDispatch } from '@/redux/store';

const alarmType = {
  movie: { idx: 0, text: '영화 알림' },
  musical: { idx: 1, text: '뮤지컬 알림' },
  opera: { idx: 2, text: '오페라 알림' },
  exhibition: { idx: 3, text: '전시회 알림' },
};

const alarmSettingList = [
  { value: 'ONE_HOUR', text: '1시간 전' },
  { value: 'THR_HOUR', text: '3시간 전' },
  { value: 'SIX_HOUR', text: '6시간 전' },
  { value: 'TWE_HOUR', text: '12시간 전' },
  { value: 'ONE_DAY', text: '1일 전' },
  { value: 'THR_DAY', text: '3일 전' },
  { value: 'SEV_DAY', text: '7일 전' },
];

export default function Page() {
  console.log('render');
  const category = useParams().category as string;
  const dispatch = useAppDispatch();
  const [alarm, setAlarm] = useState<boolean | null>(null);
  const [alarmList, setAlarmList] = useState<string[]>(null);
  const [isFirst, setIsFirst] = useState(true);

  const handleToggleClick = async () => {
    if (alarm) {
      setAlarmList((prev) => {
        const newAlarmList = [...prev];
        newAlarmList[alarmType[category].idx] = 'ZERO_HOUR';
        return newAlarmList;
      });
    }
    setAlarm((prev) => !prev);
  };

  const handleAlarmChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value;

    setAlarmList((prev) => {
      const newAlarmList = [...prev];
      newAlarmList[alarmType[category].idx] = newValue;
      return newAlarmList;
    });
  };

  const getAlarmSetting = async () => {
    const response = await apiRequest(
      `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/getAlarmTimings`,
      dispatch,
      {
        method: 'GET',
      }
    );

    if (!response.ok) {
      notFound();
    }

    const data = await response.json();
    setAlarmList(data);
    setAlarm(data[alarmType[category].idx] !== 'ZERO_HOUR');
  };

  const setNewAlarmSetting = async () => {
    console.log(alarmList);
    const response = await apiRequest(
      `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/${category}/changeAlarm`,
      dispatch,
      {
        method: 'POST',
        body: JSON.stringify(alarmList),
      }
    );

    if (!response.ok) {
      return <div>에러</div>;
    }

    const data = await response.json();
  };

  useEffect(() => {
    getAlarmSetting();
  }, []);

  useEffect(() => {
    // TODO : 첫화면 mount시 setNewAlarmSetting 호출하는 이슈 수정

    if (alarmList) {
      if (isFirst) {
        setIsFirst(false);
      } else {
        setNewAlarmSetting();
      }
    }
  }, [alarmList]);

  return (
    <div className={styles.container}>
      <div className={styles.div__about}>
        회원님이 설정한 시간에 따라, 관심 있는 콘텐츠의 개봉/티켓 오픈일이
        다가오면 알림을 보내드립니다. <br /> <br />
        원하는 시간을 설정하고, 중요한 순간을 놓치지 마세요!
      </div>

      <form className={styles.form__setting}>
        <div className={styles.div__toggle_wrapper}>
          <span>{alarmType[category].text}</span>
          <motion.div
            className={styles.div__toggle_bar}
            onClick={handleToggleClick}
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
              {alarmSettingList.map((type, idx) => {
                if (category === 'movie') {
                  if (![4, 5, 6].includes(idx)) {
                    return null;
                  }
                }
                return (
                  <div key={'div_' + type.value}>
                    <input
                      className={styles.input__radio}
                      id={'setting_' + type.value}
                      type="radio"
                      value={type.value}
                      name="alarmSetting"
                      checked={
                        alarmList[alarmType[category].idx] === type.value
                      }
                      onChange={handleAlarmChange}
                    />
                    <label
                      className={styles.label__radio}
                      htmlFor={'setting_' + type.value}
                    >
                      {type.text}
                    </label>
                  </div>
                );
              })}
            </motion.div>
          )}
        </AnimatePresence>
      </form>
    </div>
  );
}
