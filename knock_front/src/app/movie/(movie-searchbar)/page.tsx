import styles from './page.module.scss';
import MovieItem from '@/components/MovieItem';
import { IMovie } from '@/types';
import SearchBar from '@/components/SearchBar';
import CategoryList from '@/components/CategoryList';
import { notFound } from 'next/navigation';

export default async function Page() {
  const allMovieResponse = await fetch(
    `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/movie`
    // { next: { revalidate: 86400 } }
  );
  if (!allMovieResponse.ok) {
    notFound();
  }
  const allMovies: IMovie[] = await allMovieResponse.json();

  return (
    <>
      <div className={styles.search_bar__container}>
        <SearchBar searchTitle="" searchCategory="" />
        <CategoryList searchTitle="" searchCategory="" />
      </div>

      <div className={styles.div__movie_list}>
        {allMovies.map((movie) => (
          <MovieItem key={movie.movieId} {...movie} display={true} />
        ))}
      </div>
    </>
  );
}
