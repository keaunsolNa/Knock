import Header from '@/components/Header';
import { ReactNode } from 'react';
import styles from './layout.module.scss';

export default function Layout({ children }: { children: ReactNode }) {
  return (
    <div className={styles.container}>
      <Header />
      {children}
    </div>
  );
}
