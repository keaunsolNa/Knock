import Header from '@/components/Header';
import SearchBar from '@/components/SearchBar';
import { ReactNode, Suspense } from 'react';

export default function Layout({ children }: { children: ReactNode }) {
  return (
    <div>
      <Header />
      <Suspense fallback={<div>movie Loading...</div>}>
        <SearchBar />
      </Suspense>
      {children}
    </div>
  );
}
