import CategoryNav from '@/components/performingArts/CategoryNav';
import styles from './page.module.scss';
import { TiTicket } from 'react-icons/ti';
import { IPerformingArts } from '@/types';
import Image from 'next/image';
import Link from 'next/link';
import { genreToLink } from '@/utils/typeToText';

export default async function Page() {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/performingArts/upcomingList`);

  if (!response.ok) {
    throw new Error('performingArts SSR 페이지 API 요청 실패');
  }

  const data: IPerformingArts[] = await response.json();

  const posterGroups = Array.from({ length: 3 }, (_, i) => data.slice(i * 6, i * 6 + 6));

  return (
    <div className={styles.container}>
      <CategoryNav />

      <section className={styles.section__upcoming}>
        <div>
          <div className={styles.div__title}>
            <TiTicket />
            <p>UPCOMING</p>
          </div>
          <p className={styles.p__sub}>다가오는 일주일 안에 안에 볼 수 있는 공연들을 모아봤어요!</p>
        </div>

        <div className={styles.div__view_box}>
          <div className={styles.div__scroll_box}>
            {posterGroups.map((array: IPerformingArts[], idx) => (
              <div className={styles.div__poster_list} key={`posterBox_${idx}`}>
                {array.map((performance) => (
                  <Link href={`/performingArts/${genreToLink[performance.categoryLevelTwo.nm]}/${performance.id}`} key={performance.id}>
                    <Image src={performance.poster} width={500} height={700} alt={`${performance.name}의 포스터 이미지`} />
                  </Link>
                ))}
              </div>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
}
