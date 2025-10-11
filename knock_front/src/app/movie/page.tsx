import { ICategory, IMovie } from '@/types';
import MovieClient from '@/components/movie/MovieClient';

export default async function Page() {
  // vercel CDN 캐싱으로 캐싱 중복을 피하기위해 페이지 동적 생성
  const [movieRes, categoryRes] = await Promise.all([
    fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/movie`, { cache: 'no-store' }),
    fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/movie/getCategory`, { cache: 'no-store' }),
  ]);

  if (!movieRes.ok || !categoryRes.ok) {
    throw new Error('movie SSR 페이지 요청 실패');
  }

  const allMovies: IMovie[] = await movieRes.json();
  const categories: ICategory[] = (await categoryRes.json()).data;

  categories.sort((a, b) => (a.movies.length >= b.movies.length ? -1 : 1));

  return <MovieClient allMovies={allMovies} categories={categories} />;
}
