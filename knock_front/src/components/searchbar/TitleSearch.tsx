'use client';

import { FormEvent } from 'react';
import { useState } from 'react';
import { IoSearchSharp } from 'react-icons/io5';
import { GrPowerReset } from 'react-icons/gr';
import styles from '@/styles/components/searchbar/title-search.module.scss';

export default function TitleSearch({
  searchTitle,
  searchFilter,
  setSearchTitle,
  resetAll,
}: {
  searchTitle: string;
  searchFilter: string;
  setSearchTitle: Function;
  resetAll: Function;
}) {
  const [curTitle, setCurTitle] = useState<string>(searchTitle || '');

  const submitHandler = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setSearchTitle(curTitle);
  };

  const resetOnClick = () => {
    setCurTitle('');
    resetAll();
  };

  return (
    <div className={styles.container}>
      <form onSubmit={submitHandler}>
        <input value={curTitle} onChange={(e) => setCurTitle(e.currentTarget.value)} placeholder="제목으로 검색해보세요" />
        <div onClick={() => setSearchTitle(curTitle)}>
          <IoSearchSharp />
        </div>
      </form>

      <div className={`${styles.btn__reset} ${(searchTitle || searchFilter) && styles.btn__reset_filtered}`} onClick={resetOnClick}>
        <GrPowerReset />
      </div>
    </div>
  );
}
