'use client';

import { RootState } from '@/redux/store';
import styles from '@/styles/error.module.scss';
import { useRouter } from 'next/navigation';
import { useSelector } from 'react-redux';

export default function Error() {
  const redirectUrl = useSelector((state: RootState) => state.auth.redirectUrl);
  const router = useRouter();

  return (
    <div className={styles.container}>
      <img src="/images/error.png" />
      <p className={styles.p__bold}>정보를 불러올 수 없어요 😥</p>
      <p className={styles.p__default}>
        죄송합니다 <br />
        잠시 후에 다시 시도해 주세요.
      </p>
      <button
        onClick={() => {
          if (redirectUrl) {
            router.push(redirectUrl);
          } else {
            router.push('/intro');
          }
        }}
        className={styles.btn__return}
      >
        메인 화면으로 돌아가기
      </button>
    </div>
  );
}
