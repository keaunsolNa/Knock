import { IMovie } from '@/types';
import styles from './page.module.scss';
import Image from 'next/image';
import CategoryItem from '@/components/CategoryItem';
import { BsBell, BsBellFill } from 'react-icons/bs';

export default async function Page({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = await params;
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/movie/getDetail?movieId=${id}`
  );

  if (!response.ok) {
    return <div>페이지 오류</div>;
  }

  const movieDetail: IMovie = await response.json();
  console.log(movieDetail);

  return (
    <div>
      <div
        className={styles.img__cover_container}
        style={{ backgroundImage: `url('${movieDetail.posterBase64}')` }}
      >
        <Image
          className={styles.img__poster}
          src={movieDetail.posterBase64}
          alt={`${movieDetail.movieNm}의 포스터이미지`}
          width={300}
          height={400}
          priority
        />

        <button className={styles.btn__subscribe}>
          <BsBellFill />
          <span>
            {movieDetail.favorites ? movieDetail.favorites.length : '0'}
          </span>
        </button>
      </div>

      <div className={styles.div__details}>
        <section className={styles.main_info__wrapper}>
          <div className={styles.div__category_list}>
            {movieDetail.categoryLevelTwo.map((category) => {
              return (
                <CategoryItem
                  key={category.id}
                  type="detail"
                  categoryNm={category.nm}
                />
              );
            })}
          </div>
          <h2 className={styles.title}>{movieDetail.movieNm}</h2>
          <h3 className={styles.open_date}>{movieDetail.openingTime} 개봉</h3>
        </section>

        <h3 className={styles.section_title}>기본정보</h3>
        <section className={styles.table}>
          <div className={styles.table_tr}>
            <div className={styles.table_th}>감독</div>
            <div className={styles.table_td}>{movieDetail.directors}</div>
          </div>
          <div className={styles.table_tr}>
            <div className={styles.table_th}>출연진</div>
            <div className={styles.table_td}>
              {movieDetail.actors.join(',')}
            </div>
          </div>
          <div className={styles.table_tr}>
            <div className={styles.table_th}>러닝타임</div>
            <div className={styles.table_td}>{movieDetail.runningTime}분</div>
          </div>
        </section>

        <h3 className={styles.section_title}>줄거리</h3>
        <p className={styles.plot}>{movieDetail.plot}</p>
      </div>
    </div>
  );
}
