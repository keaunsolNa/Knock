'use client';

import { FormEvent } from 'react';
import { useRouter } from 'next/navigation';
import { useState } from 'react';
import { IoSearchSharp } from 'react-icons/io5';
import { ISearch } from '@/types';
import { GrPowerReset } from 'react-icons/gr';
import styles from '@/styles/components/searchbar/title-search.module.scss';

export default function TitleSearch({ searchTitle, searchCategory }: ISearch) {
  const [title, setTitle] = useState<string>(searchTitle);
  const router = useRouter();
  const routerOption = { scroll: false };

  const routeMove = () => {
    if (title === '' && searchCategory === '') {
      router.push('/movie', routerOption);
    }
    router.push(
      `/movie/search?title=${title}&category=${searchCategory}`,
      routerOption
    );
  };

  const submitHandler = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    routeMove();
  };

  const resetBtnOnClick = () => {
    setTitle('');
    router.push('/movie', routerOption);
  };

  const resetBtnDefault = `${styles.btn__reset}`;
  const resetBtnFiltered = `${styles.btn__reset} ${styles.btn__reset_filtered}`;

  return (
    <div className={styles.container}>
      <form onSubmit={submitHandler}>
        <input
          value={title}
          onChange={(e) => setTitle(e.currentTarget.value)}
          placeholder="영화명으로 검색해보세요"
        />
        <button onClick={routeMove}>
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
