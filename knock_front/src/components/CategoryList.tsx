'use client';

import styles from '@/styles/components/category-list.module.scss';
import { ICategory } from '@/types';
import { useRouter } from 'next/navigation';
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
  const router = useRouter();
  const [categories, setCategories] = useState<ICategory[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      const response = await fetch(`api/movie/getCategory`);

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

  const categoryClickHandler = (e: React.MouseEvent<HTMLSpanElement>) => {
    const clickedCategoryId = e.currentTarget.getAttribute(
      'category-id'
    ) as string;

    if (searchCategory === '') {
      router.push(
        `/movie/search?title=${title}&categories=${clickedCategoryId}`
      );
    } else {
      const categoryList = searchCategory.split(',');
      const idx = categoryList.indexOf(clickedCategoryId);
      const isIncludesCategory = idx !== -1;

      if (isIncludesCategory) {
        categoryList.splice(idx, 1);

        if (categoryList.length === 0) {
          router.push('/movie');
          return;
        }
      } else {
        categoryList.push(clickedCategoryId);
      }

      router.push(
        `/movie/search?title=${title}&categories=${categoryList.join(',')}`
      );
    }
  };

  const isCategoryInSearchList = (id: string) =>
    searchCategory.split(',').includes(id);
  return (
    <div className={styles.div__category}>
      <div className={styles.list}>
        {categories.map((category) => (
          <span
            key={category.categoryId}
            category-id={category.categoryId}
            onClick={categoryClickHandler}
            className={
              isCategoryInSearchList(category.categoryId)
                ? categorySelected
                : categoryDefault
            }
          >
            {category.categoryNm}
          </span>
        ))}
      </div>
    </div>
  );
}
