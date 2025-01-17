import styles from '@/styles/components/category-item.module.scss';
import Link from 'next/link';

type categoryType = 'searchbar' | 'detail';

interface ICategoryItem {
  type: categoryType;
  searchTitle?: string;
  searchCategory?: string;
  categoryNm: string;
  categoryId?: string;
}

export default function CategoryItem({
  type,
  searchTitle,
  searchCategory,
  categoryNm,
  categoryId,
}: ICategoryItem) {
  if (type === 'detail') {
    return <span className={styles.detail}>{categoryNm}</span>;
  } else if (type === 'searchbar') {
    return (
      <Link
        href={`/movie/search?title=${searchTitle}&category=${searchCategory === categoryId ? '' : categoryId}`}
        scroll={false}
        className={
          searchCategory === categoryId
            ? styles.searchbar_select
            : styles.searchbar
        }
      >
        {categoryNm}
      </Link>
    );
  }
}
