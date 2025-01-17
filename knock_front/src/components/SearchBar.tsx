'use client';

import styles from '@/styles/components/search-bar.module.scss';
import { IoSearchSharp } from 'react-icons/io5';
import { GrPowerReset } from 'react-icons/gr';
import React, { FormEvent, useState } from 'react';
import { useRouter } from 'next/navigation';

const resetBtnDefault = `${styles.btn__reset}`;
const resetBtnFiltered = `${styles.btn__reset} ${styles.btn__reset_filtered}`;

export default function SearchBar({
  searchTitle,
  searchCategory,
}: {
  searchTitle: string;
  searchCategory: string;
}) {
  const [title, setTitle] = useState<string>(searchTitle);
  const router = useRouter();
  const routerOption = { scroll: false };

  const submitHandler = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (title === '' && searchCategory === '') {
      router.push('/movie', routerOption);
    }
    router.push(
      `/movie/search?title=${title}&category=${searchCategory}`,
      routerOption
    );
  };

  const searchBtnOnClick = () => {
    if (title === '' && searchCategory === '') {
      router.push('/movie', routerOption);
    }

    router.push(
      `/movie/search?title=${title}&category=${searchCategory}`,
      routerOption
    );
  };

  const resetBtnOnClick = () => {
    setTitle('');
    router.push('/movie', routerOption);
  };

  return (
    <div className={styles.container}>
      <form onSubmit={submitHandler}>
        <input
          value={title}
          onChange={(e) => setTitle(e.currentTarget.value)}
          placeholder="영화명으로 검색해보세요"
        />
        <button onClick={searchBtnOnClick}>
          <IoSearchSharp />
        </button>
      </form>

      <button
        className={
          searchTitle || searchCategory ? resetBtnFiltered : resetBtnDefault
        }
        onClick={resetBtnOnClick}
      >
        <GrPowerReset />
      </button>
    </div>
  );
}
