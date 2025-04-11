import PerformClient from '@/components/performingArts/PerformClient';
import { IPerformingArts } from '@/types';
import { performingArtsArea } from '@/utils/typeToText';

export default async function Page({ params }: { params: Promise<{ category: string }> }) {
  const { category } = await params;
  const today = new Date().toISOString().split('T')[0];

  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/performingArts/category?category=${category}&cacheKey=${today}`,
    {
      next: { revalidate: 86400 }, // 24시간 (1일)
    }
  );

  if (!response.ok) {
    throw new Error(`performingArts/${category} SSR 페이지 API 요청 실패`);
  }

  const allPerform: IPerformingArts[] = await response.json();
  const filters = performingArtsArea.map((areaText, idx) => {
    return {
      categoryNm: areaText,
      categoryId: `area_${idx}`,
    };
  });

  return <PerformClient allPerform={allPerform} filters={filters} genre={category} />;
}
