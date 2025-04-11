'use client';

import { useState } from 'react';
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

  const resetAll = () => {
    setSearchTitle('');
    setSearchFilter('');
  };

  const titleFilter = (performList: IPerformingArts[]) => {
    const options = {
      keys: ['name'],
      includeScore: true,
      threshold: 0.3,
    };

    const performFuse = new Fuse(performList, options);
    const titleSearchResult = performFuse.search(searchTitle);

    return titleSearchResult.map(({ item }) => item);
  };

  const categoryFilter = (performList: IPerformingArts[]) => {
    return performList.filter((perform) => areaToCode[perform.area] === searchFilter[5]);
  };

  let filteredList: IPerformingArts[] = allPerform;

  if (searchFilter && searchFilter !== '') {
    filteredList = categoryFilter(filteredList);
  }

  if (searchTitle && searchTitle !== '') {
    filteredList = titleFilter(filteredList);
  }

  return (
    <>
      <div className={styles.container}>
        <TitleSearch searchTitle={searchTitle} searchFilter={searchFilter} setSearchTitle={setSearchTitle} resetAll={resetAll} />
        <CategoryList categories={filters} searchFilter={searchFilter} setSearchFilter={setSearchFilter} />
      </div>
      <ContentList itemList={filteredList} category="performingArts" genre={genre} />
    </>
  );
}
