'use client';

import styles from './page.module.scss';
import { BeatLoader } from 'react-spinners';

export default function Page() {
  return (
    <div className={styles.container}>
      <BeatLoader size={30} color={'#f45f41'} />
      <p>로그인 중...</p>
    </div>
  );
}
