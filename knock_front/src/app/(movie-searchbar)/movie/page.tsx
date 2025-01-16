import MovieItem from '@/components/MovieItem';
import { IMovie } from '@/types';

export default async function Page() {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/movie`,
    { next: { revalidate: 86400 } }
  );

  if (!response.ok) {
    return <div>에러발생</div>;
  }

  const allMovies: IMovie[] = await response.json();

  return (
    <>
      {allMovies.map((movie) => (
        <MovieItem key={movie.movieId} {...movie} display={true} />
      ))}
    </>
  );
}
