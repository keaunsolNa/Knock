'use client';

import styles from '@/styles/components/category-list.module.scss';
import { ICategory } from '@/types';
import Link from 'next/link';
import { useEffect, useState } from 'react';

const categoryDefault = `${styles.item}`;
const categorySelected = `${styles.item} ${styles.item_select}`;

export default function CategoryList({
  searchCategory,
  title,
}: {
  searchCategory: string;
  title: string;
}) {
  const [categories, setCategories] = useState<ICategory[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      const response = await fetch(`/api/movie/getCategory`);

      if (!response.ok) {
        return <div>오류가 발생했습니다...</div>;
      }
      const categoryList: ICategory[] = await (await response.json()).data;
      categoryList.sort((a, b) =>
        a.movies.length >= b.movies.length ? -1 : 1
      );
      setCategories(categoryList);
    };

    fetchData();
  }, []);

  return (
    <div className={styles.div__category}>
      <div className={styles.list}>
        {categories.map((category) => (
          <Link
            key={category.categoryId}
            category-id={category.categoryId}
            href={`/movie/search?title=${title}&category=${searchCategory === category.categoryId ? '' : category.categoryId}`}
            className={
              searchCategory === category.categoryId
                ? categorySelected
                : categoryDefault
            }
          >
            {category.categoryNm}
          </Link>
        ))}
      </div>
    </div>
  );
}
