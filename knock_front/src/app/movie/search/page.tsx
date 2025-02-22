import { IMovie } from '@/types';
import Fuse from 'fuse.js';
import SearchBar from '@/components/searchbar/SearchBar';
import ContentList from '@/components/ContentList';

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

  const categoryFilter = (movieList: IMovie[]) => {
    return movieList.filter((movie) =>
      movie.categoryLevelTwo.some(({ id }) => id === category)
    );
  };

  const titleFilter = (movieList: IMovie[]) => {
    const options = {
      keys: ['movieNm'],
      includeScore: true,
      threshold: 0.3,
    };

    const movieFuse = new Fuse(movieList, options);
    const titleSearchResult = movieFuse.search(title);

    return titleSearchResult.map(({ item }) => item);
  };

  let filteredList: IMovie[] = allMovies;

  if (category !== '') {
    filteredList = categoryFilter(filteredList);
  }

  if (title !== '') {
    filteredList = titleFilter(filteredList);
  }

  return (
    <>
      <SearchBar searchTitle={title} searchCategory={category} />
      <ContentList itemList={filteredList} category="movie" />
    </>
  );
}
