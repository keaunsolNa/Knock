'use client';

import { FormEvent } from 'react';
import { usePathname, useRouter } from 'next/navigation';
import { useState } from 'react';
import { IoSearchSharp } from 'react-icons/io5';
import { ISearch } from '@/types';
import { GrPowerReset } from 'react-icons/gr';
import styles from '@/styles/components/searchbar/title-search.module.scss';

export default function TitleSearch({ link, searchTitle, searchFilter }: ISearch) {
  const [title, setTitle] = useState<string>(searchTitle);
  const router = useRouter();
  const routerOption = { scroll: false };

  const routeMove = () => {
    if (link === 'movie') {
      router.push(`/movie?title=${title}&filter=${searchFilter}`, routerOption);
    } else {
      router.push(`/${link}?title=${title}&filter=${searchFilter}`, routerOption);
    }
  };

  const submitHandler = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    routeMove();
  };

  const resetBtnOnClick = () => {
    setTitle('');

    if (link === 'movie') {
      router.push('/movie', routerOption);
    } else {
      router.push(`/${link}`, routerOption);
    }
  };

  const resetBtnDefault = `${styles.btn__reset}`;
  const resetBtnFiltered = `${styles.btn__reset} ${styles.btn__reset_filtered}`;

  return (
    <div className={styles.container}>
      <form onSubmit={submitHandler}>
        <input value={title} onChange={(e) => setTitle(e.currentTarget.value)} placeholder="제목으로 검색해보세요" />
        <button onClick={routeMove}>
          <IoSearchSharp />
        </button>
      </form>

      <button className={searchTitle || searchFilter ? resetBtnFiltered : resetBtnDefault} onClick={resetBtnOnClick}>
        <GrPowerReset />
      </button>
    </div>
  );
}
