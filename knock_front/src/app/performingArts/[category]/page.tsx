import ContentList from '@/components/ContentList';
import SearchBar from '@/components/searchbar/SearchBar';
import { IPerformingArts } from '@/types';
import { areaToCode } from '@/utils/typeToText';
import Fuse from 'fuse.js';

export default async function Page({
  params,
  searchParams,
}: {
  params: Promise<{ category: string }>;
  searchParams: Promise<{ title: string; filter: string }>;
}) {
  const { category } = await params;
  const { title, filter } = await searchParams;

  const response = await fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/performingArts/category?category=${category}`);

  if (!response.ok) {
    throw new Error(`performingArts/${category} SSR 페이지 API 요청 실패`);
  }

  const allPerform: IPerformingArts[] = await response.json();

  const categoryFilter = (performList: IPerformingArts[]) => {
    return performList.filter((perform) => areaToCode[perform.area] === filter[5]);
  };

  const titleFilter = (performList: IPerformingArts[]) => {
    const options = {
      keys: ['name'],
      includeScore: true,
      threshold: 0.3,
    };

    const performFuse = new Fuse(performList, options);
    const titleSearchResult = performFuse.search(title);

    return titleSearchResult.map(({ item }) => item);
  };

  let filteredList: IPerformingArts[] = allPerform;

  if (filter && filter !== '') {
    filteredList = categoryFilter(filteredList);
  }

  if (title && title !== '') {
    filteredList = titleFilter(filteredList);
  }

  return (
    <>
      <SearchBar link={`performingArts/${category}`} searchTitle={title} searchFilter={filter} />
      <ContentList itemList={filteredList} category="performingArts" genre={category} />
    </>
  );
}
