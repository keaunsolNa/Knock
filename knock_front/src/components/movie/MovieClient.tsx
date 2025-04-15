'use client';

import { useEffect, useMemo, useState } from 'react';
import Fuse from 'fuse.js';
import { ICategory, IMovie } from '@/types';
import TitleSearch from '../searchbar/TitleSearch';
import CategoryList from '../searchbar/CategoryList';
import ContentList from '../ContentList';
import styles from '@/styles/components/searchbar/search-bar.module.scss';

interface MovieClientProps {
  allMovies: IMovie[];
  categories: ICategory[];
}

export default function MovieClient({ allMovies, categories }: MovieClientProps) {
  const [searchTitle, setSearchTitle] = useState('');
  const [searchFilter, setSearchFilter] = useState('');

  // 초기 뷰포트 문제 해결용 리사이즈 트리거
  useEffect(() => {
    window.dispatchEvent(new Event('resize'));
  }, []);

  const resetAll = () => {
    setSearchTitle('');
    setSearchFilter('');
  };

  const filteredMovies: IMovie[] = useMemo(() => {
    let result = [...allMovies];

    if (searchFilter) {
      result = result.filter((movie) => movie.categoryLevelTwo?.some(({ id }) => id === searchFilter));
    }

    if (searchTitle) {
      const fuse = new Fuse(result, {
        keys: ['movieNm'],
        includeScore: true,
        threshold: 0.3,
      });
      result = fuse.search(searchTitle).map(({ item }) => item);
    }
    return result;
  }, [allMovies, searchTitle, searchFilter]);

  return (
    <>
      <div className={styles.container}>
        <TitleSearch searchTitle={searchTitle} searchFilter={searchFilter} setSearchTitle={setSearchTitle} resetAll={resetAll} />
        <CategoryList categories={categories} searchFilter={searchFilter} setSearchFilter={setSearchFilter} />
      </div>
      <ContentList itemList={filteredMovies} category="movie" />
    </>
  );
}
