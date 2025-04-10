import { IMovie } from '@/types';
import SearchBar from '@/components/searchbar/SearchBar';
import ContentList from '@/components/ContentList';
import Fuse from 'fuse.js';

export default async function Page({ searchParams }: { searchParams: Promise<{ title: string; filter: string }> }) {
  const { title, filter } = await searchParams;

  const response = await fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/movie`);

  if (!response.ok) {
    throw new Error('movie SSR 페이지 API 요청 실패');
  }

  const allMovies: IMovie[] = await response.json();

  const categoryFilter = (movieList: IMovie[]) => {
    return movieList.filter((movie) => movie.categoryLevelTwo?.some(({ id }) => id === filter));
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

  if (filter && filter !== '') {
    filteredList = categoryFilter(filteredList);
  }

  if (title && title !== '') {
    filteredList = titleFilter(filteredList);
  }

  return (
    <>
      <SearchBar link="movie" searchTitle={title} searchFilter={filter} />
      <ContentList itemList={filteredList} category="movie" />
    </>
  );
}
