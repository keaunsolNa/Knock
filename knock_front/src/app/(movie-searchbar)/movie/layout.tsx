import Header from '@/components/Header';
import SearchBar from '@/components/SearchBar';
import { ReactNode, Suspense } from 'react';
import styles from './layout.module.scss';

export default function Layout({ children }: { children: ReactNode }) {
  return (
    <div>
      <Header />
      <Suspense fallback={<div>category Loading...</div>}>
        <SearchBar />
      </Suspense>
      <div className={styles.div__movie_list}>{children}</div>
    </div>
  );
}
