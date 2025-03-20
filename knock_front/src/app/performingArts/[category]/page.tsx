import ContentList from '@/components/ContentList';
import SearchBar from '@/components/searchbar/SearchBar';
import { IPerformingArts } from '@/types';
import { performingArtsGenreToText } from '@/utils/typeToText';
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

  const response = await fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/performingArts/category?category=${category}`, {
    next: { revalidate: 86400 },
  });

  if (!response.ok) {
    return <div>오류 발생</div>;
  }

  const allPerform: IPerformingArts[] = await response.json();

  const categoryEngToNm = (address: string) => {
    const addressToEnum = address.replace(/([a-z])([A-Z])/g, '$1_$2').toUpperCase();
    return performingArtsGenreToText[addressToEnum];
  };

  const categoryFilter = (performList: IPerformingArts[]) => {
    return performList.filter((perform) => perform.categoryLevelTwo.some(({ nm }) => nm === categoryEngToNm(filter)));
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
