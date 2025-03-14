import styles from '@/styles/components/searchbar/category-list.module.scss';
import CategoryItem from '../CategoryItem';
import { ICategory, ISearch } from '@/types';

export default async function CategoryList({ searchTitle, searchCategory }: ISearch) {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/movie/getCategory`, { next: { revalidate: 86400 } });

  if (!response.ok) {
    return null;
  }

  const categoryList: ICategory[] = await (await response.json()).data;

  categoryList.sort((a, b) => (a.movies.length >= b.movies.length ? -1 : 1));

  return (
    <div className={styles.div__category}>
      <div className={styles.list}>
        {categoryList.map((category) => (
          <CategoryItem
            key={category.categoryId}
            type="searchbar"
            searchTitle={searchTitle}
            searchCategory={searchCategory}
            categoryNm={category.categoryNm}
            categoryId={category.categoryId}
          />
        ))}
      </div>
    </div>
  );
}
