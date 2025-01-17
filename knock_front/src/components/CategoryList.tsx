import styles from '@/styles/components/category-list.module.scss';
import CategoryItem from './CategoryItem';
import { ICategory } from '@/types';

export default async function CategoryList({
  searchTitle,
  searchCategory,
}: {
  searchTitle: string;
  searchCategory: string;
}) {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/movie/getCategory`
  );

  if (!response.ok) {
    return <div>오류가 발생했습니다...</div>;
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
