import { ISearch } from '@/types';
import CategoryList from './CategoryList';
import TitleSearch from './TitleSearch';
import styles from '@/styles/components/searchbar/search-bar.module.scss';

export default function SearchBar({ searchTitle, searchCategory }: ISearch) {
  return (
    <div className={styles.container}>
      <TitleSearch searchTitle={searchTitle} searchCategory={searchCategory} />
      <CategoryList searchTitle={searchTitle} searchCategory={searchCategory} />
    </div>
  );
}
