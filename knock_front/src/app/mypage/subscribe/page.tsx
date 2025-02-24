import { IMovie } from '@/types';
import styles from './page.module.scss';
import MovieItem from '@/components/MovieItem';

export default function Page() {
  const subscribeMovie: IMovie[] = [];

  return (
    <div className={styles.container}>
      {subscribeMovie.length === 0 ? (
        <h3>구독 목록이 없습니다</h3>
      ) : (
        <>
          <h5 className={styles.h5__total}>(총 {subscribeMovie.length}개)</h5>
          {subscribeMovie.map((movie) => {
            return <MovieItem key={movie.movieId} {...movie} setAlarm={true} />;
          })}
        </>
      )}
    </div>
  );
}
