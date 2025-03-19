import CategoryNav from '@/components/performingArts/CategoryNav';
import styles from './page.module.scss';
import { TiTicket } from 'react-icons/ti';
import { IPerformingArts } from '@/types';
import Image from 'next/image';

export default async function Page() {
  // const response = await fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/performingArts/upcomingList`);

  // const data: IPerformingArts[] = await response.json();
  const viewPoster = [];

  // for (let i = 0; i < 3; i++) {
  //   const slice = data.splice(0, 6);
  //   viewPoster.push(slice);
  // }
  // console.log(response);
  // console.log(data);
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
            {viewPoster.map((array: IPerformingArts[]) => (
              <div className={styles.div__poster_list}>
                {array.map((performance) => (
                  <div key={performance.id}>
                    <Image src={performance.poster} width={500} height={700} alt={`${performance.name}의 포스터 이미지`} />
                  </div>
                ))}
              </div>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
}
