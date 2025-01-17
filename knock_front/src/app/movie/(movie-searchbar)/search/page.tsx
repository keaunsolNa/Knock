import MovieItem from '@/components/MovieItem';
import { ICategoryLevelTwo, IMovie } from '@/types';
import Fuse from 'fuse.js';
import styles from './page.module.scss';
import SearchBar from '@/components/SearchBar';
import CategoryList from '@/components/CategoryList';

export default async function Page({
  searchParams,
}: {
  searchParams: Promise<{ title: string; category: string }>;
}) {
  const { title, category } = await searchParams;

  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/movie`,
    {
      next: { revalidate: 86400 },
    }
  );

  if (!response.ok) {
    return <div>오류 발생</div>;
  }

  const allMovies: IMovie[] = await response.json();

  const isMovieInTitleSearchResult = (movieId: string) => {
    if (title === '') {
      return true;
    }
    const options = {
      keys: ['movieNm'],
      includeScore: true,
      threshold: 0.3,
    };

    const movieFuse = new Fuse(allMovies, options);
    const titleSearchResult = movieFuse.search(title);

    if (titleSearchResult.length > 0) {
      return (
        titleSearchResult.findIndex(
          (result) => result.item.movieId === movieId
        ) !== -1
      );
    } else {
      return false;
    }
  };

  const isMovieInCategory = (categoryList: ICategoryLevelTwo[]) => {
    if (category === '') {
      return true;
    }
    return categoryList.some((categoryItem) => categoryItem.id === category);
  };

  return (
    <>
      <div className={styles.search_bar__container}>
        <SearchBar searchTitle={title} searchCategory={category} />
        <CategoryList searchTitle={title} searchCategory={category} />
      </div>

      <div className={styles.div__movie_list}>
        {allMovies.map((movie) => (
          <MovieItem
            key={movie.movieId}
            {...movie}
            display={
              isMovieInTitleSearchResult(movie.movieId) &&
              isMovieInCategory(movie.categoryLevelTwo)
            }
          />
        ))}
      </div>
    </>
  );
}
