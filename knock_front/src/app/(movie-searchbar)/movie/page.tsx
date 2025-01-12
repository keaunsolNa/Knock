import MovieItem from '@/components/MovieItem';
import SearchBar from '@/components/SearchBar';
import styles from './page.module.scss';

export default function Page() {
  return (
    <div className={styles.container}>
      <SearchBar />
      <div className={styles.div__movie_list}>
        <MovieItem />
        <MovieItem />
        <MovieItem />
        <MovieItem />
        <MovieItem />
      </div>
    </div>
  );
}
