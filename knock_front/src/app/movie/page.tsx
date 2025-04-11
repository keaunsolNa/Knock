import { ICategory, IMovie } from '@/types';
import MovieClient from '@/components/movie/MovieClient';

export default async function Page() {
  const today = new Date().toISOString().split('T')[0];
  const cacheOption = {
    next: { revalidate: 86400 }, // 24시간 (1일)
  };

  const [movieRes, categoryRes] = await Promise.all([
    fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/movie?cacheKey=${today}`, cacheOption),
    fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/movie/getCategory?cacheKey=${today}`, cacheOption),
  ]);

  if (!movieRes.ok || !categoryRes.ok) {
    throw new Error('movie SSR 페이지 요청 실패');
  }

  const allMovies: IMovie[] = await movieRes.json();
  const categories: ICategory[] = (await categoryRes.json()).data;

  categories.sort((a, b) => (a.movies.length >= b.movies.length ? -1 : 1));

  return <MovieClient allMovies={allMovies} categories={categories} />;
}
