'use client';

import { setModal } from '@/redux/modalSlice';
import { RootState, useAppDispatch } from '@/redux/store';
import styles from '@/styles/components/unauthorized-modal.module.scss';
import { useRouter } from 'next/navigation';
import { useEffect } from 'react';
import { useSelector } from 'react-redux';

export function UnauthorizedModal() {
  const router = useRouter();
  const dispatch = useAppDispatch();
  const isOpen = useSelector((state: RootState) => state.modal.isOpen);

  useEffect(() => {
    if (isOpen) {
      document.body.style.overflow = 'hidden'; // 스크롤 막기
    } else {
      document.body.style.overflow = ''; // 원래대로 복구
    }

    return () => {
      document.body.style.overflow = ''; // 언마운트 시 복구
    };
  }, [isOpen]);

  return (
    <>
      {isOpen && (
        <div className={styles.container}>
          <div className={styles.div__alert_box}>
            <h2>세션 만료</h2>
            <p>
              로그인 세션이 만료되었습니다. <br /> 다시 로그인해 주세요.
            </p>
            <button
              onClick={() => {
                dispatch(setModal({ isOpen: false }));
                router.push('/login');
              }}
            >
              확인
            </button>
          </div>
        </div>
      )}
    </>
  );
}
