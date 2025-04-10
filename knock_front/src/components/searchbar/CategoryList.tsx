import styles from '@/styles/components/searchbar/category-list.module.scss';
import { ICategory, ISearch } from '@/types';
import { performingArtsArea } from '@/utils/typeToText';
import Link from 'next/link';

export default async function CategoryList({ link, searchTitle, searchFilter }: ISearch) {
  let categoryList: ICategory[] = [];

  if (link === 'movie') {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/movie/getCategory`);
    if (!response.ok) {
      return null;
    }
    categoryList = await (await response.json()).data;
    categoryList.sort((a, b) => (a.movies.length >= b.movies.length ? -1 : 1));
  } else {
    categoryList = performingArtsArea.map((areaText, idx) => {
      return {
        categoryNm: areaText,
        categoryId: `area_${idx}`,
      };
    });
  }

  return (
    <div className={styles.div__category}>
      <div className={styles.list}>
        {categoryList.map((category: ICategory) => (
          <Link
            key={category.categoryId}
            href={`/${link}?title=${searchTitle}&filter=${searchFilter === category.categoryId ? '' : category.categoryId}`}
            scroll={false}
            className={`${styles.filter} ${searchFilter === category.categoryId && styles.filter_select}`}
          >
            {category.categoryNm}
          </Link>
        ))}
      </div>
    </div>
  );
}
