'use client';

import styles from '@/styles/components/search-bar.module.scss';
import { IoSearchSharp } from 'react-icons/io5';
import { GrPowerReset } from 'react-icons/gr';
import React, { useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import CategoryList from './CategoryList';

const resetBtnDefault = `${styles.btn__reset}`;
const resetBtnFiltered = `${styles.btn__reset} ${styles.btn__reset_filtered}`;

export default function SearchBar() {
  const searchParams = useSearchParams();
  const searchTitle = searchParams.get('title') || '';
  const searchCategory = searchParams.get('category') || '';

  const [title, setTitle] = useState<string>(searchTitle);
  const router = useRouter();

  const inputEnterHandler = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      router.push(`/movie/search?title=${title}&category=${searchCategory}`);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.div__title}>
        <div>
          <input
            value={title}
            onChange={(e) => setTitle(e.currentTarget.value)}
            onKeyUp={inputEnterHandler}
            placeholder="영화명으로 검색해보세요"
          />
          <IoSearchSharp />
        </div>
        <button
          className={
            searchTitle || searchCategory ? resetBtnFiltered : resetBtnDefault
          }
          onClick={() => {
            setTitle('');
            router.push('/movie');
          }}
        >
          <GrPowerReset />
        </button>
      </div>
      <CategoryList title={title} searchCategory={searchCategory} />
    </div>
  );
}
