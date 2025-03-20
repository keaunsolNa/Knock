import { ISearch } from '@/types';
import CategoryList from './CategoryList';
import TitleSearch from './TitleSearch';
import styles from '@/styles/components/searchbar/search-bar.module.scss';

export default function SearchBar({ link, searchTitle, searchFilter }: ISearch) {
  return (
    <div className={styles.container}>
      <TitleSearch link={link} searchTitle={searchTitle || ''} searchFilter={searchFilter || ''} />
      <CategoryList link={link} searchTitle={searchTitle || ''} searchFilter={searchFilter || ''} />
    </div>
  );
}
