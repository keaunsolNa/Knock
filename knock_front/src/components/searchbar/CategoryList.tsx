import { ICategory } from '@/types';
import styles from '@/styles/components/searchbar/category-list.module.scss';

export default function CategoryList({
  categories,
  searchFilter,
  setSearchFilter,
}: {
  categories: ICategory[];
  searchFilter: string;
  setSearchFilter: Function;
}) {
  return (
    <div className={styles.div__category}>
      <div className={styles.list}>
        {categories.map((category: ICategory) => (
          <span
            key={category.categoryId}
            onClick={() => setSearchFilter(searchFilter === category.categoryId ? '' : category.categoryId)}
            className={`${styles.filter} ${searchFilter === category.categoryId && styles.filter_select}`}
          >
            {category.categoryNm}
          </span>
        ))}
      </div>
    </div>
  );
}
