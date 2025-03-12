import { IMovie } from '@/types';
import SearchBar from '@/components/searchbar/SearchBar';
import { notFound } from 'next/navigation';
import ContentList from '@/components/ContentList';

export default async function Page() {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/movie`
  );

  if (!response.ok) {
    notFound();
  }

  const allMovies: IMovie[] = await response.json();
  return (
    <>
      <SearchBar searchTitle="" searchCategory="" />
      <ContentList itemList={allMovies} category="movie" />
    </>
  );
}
