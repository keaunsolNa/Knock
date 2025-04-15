'use client';

import { useEffect, useMemo, useState } from 'react';
import { ICategory, IPerformingArts } from '@/types';
import { areaToCode } from '@/utils/typeToText';
import CategoryList from '../searchbar/CategoryList';
import TitleSearch from '../searchbar/TitleSearch';
import ContentList from '../ContentList';
import Fuse from 'fuse.js';
import styles from '@/styles/components/searchbar/search-bar.module.scss';

export default function PerformClient({
  allPerform,
  filters,
  genre,
}: {
  allPerform: IPerformingArts[];
  filters: ICategory[];
  genre: string;
}) {
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

  const filteredPerform = useMemo(() => {
    let result = [...allPerform];

    if (searchFilter) {
      result = result.filter((perform) => areaToCode[perform.area] === searchFilter[5]);
    }

    if (searchTitle) {
      const fuse = new Fuse(result, {
        keys: ['name'],
        includeScore: true,
        threshold: 0.3,
      });
      result = fuse.search(searchTitle).map(({ item }) => item);
    }
    return result;
  }, [allPerform, searchTitle, searchFilter]);

  return (
    <>
      <div className={styles.container}>
        <TitleSearch searchTitle={searchTitle} searchFilter={searchFilter} setSearchTitle={setSearchTitle} resetAll={resetAll} />
        <CategoryList categories={filters} searchFilter={searchFilter} setSearchFilter={setSearchFilter} />
      </div>
      <ContentList itemList={filteredPerform} category="performingArts" genre={genre} />
    </>
  );
}
