import Header from '@/components/Header';
import styles from './layout.module.scss';
import { ReactNode } from 'react';

export default function Layout({ children }: { children: ReactNode }) {
  return (
    <div className={styles.container}>
      <Header />
      {children}
    </div>
  );
}
