import MovieItem from '@/components/MovieItem';
import styles from './page.module.scss';

export default async function Page() {

  return (
      <div>
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
