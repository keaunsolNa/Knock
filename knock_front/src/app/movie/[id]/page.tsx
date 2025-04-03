import { IMovie } from '@/types';
import styles from './page.module.scss';
import Image from 'next/image';
import SubscribeBtn from '@/components/SubscribeBtn';
import Tag from '@/components/Tag';
import Link from 'next/link';

export default async function Page({ params }: { params: Promise<{ id: string }> }) {
  const { id } = await params;
  const [movieResponse, recoResponse] = await Promise.all([
    fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/movie/getDetail?movieId=${id}`),
    fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/movie/recommend?movieId=${id}`),
  ]);

  if (!movieResponse.ok || !recoResponse.ok) {
    throw new Error(`movie/${id} SSR 페이지 API 요청 실패`);
  }

  const movieDetail: IMovie = await movieResponse.json();
  const recoMovies: IMovie[] = await recoResponse.json();

  const posterImg = () => {
    if (!movieDetail.posterBase64) {
      if (movieDetail.img) {
        return `data:image/png;base64,${movieDetail.img}`;
      } else {
        return '/images/noImage.png';
      }
    } else {
      return movieDetail.posterBase64;
    }
  };

  return (
    <div>
      <div className={styles.img__cover_container} style={{ backgroundImage: `url('${posterImg()}')` }}>
        <Image
          className={styles.img__poster}
          src={posterImg()}
          alt={movieDetail.posterBase64 || movieDetail.img ? `${movieDetail.movieNm} 포스터` : `${movieDetail.movieNm} 포스터 대체 이미지`}
          width={300}
          height={400}
          priority
        />
        <SubscribeBtn favorites={movieDetail.favoritesCount} id={movieDetail.movieId} />
      </div>

      <div className={styles.div__details}>
        <section className={styles.main_info__wrapper}>
          <div className={styles.div__category_list}>
            {movieDetail.categoryLevelTwo.map((category) => {
              return <Tag key={category.id} text={category.nm} />;
            })}
          </div>
          <h2 className={styles.title}>{movieDetail.movieNm}</h2>
          <h3 className={styles.open_date}>{`${movieDetail.openingTime}${movieDetail.openingTime !== '개봉 예정' ? ' 개봉' : ''}`}</h3>
        </section>

        <div className={styles.detail_info__wrapper}>
          <section>
            <h3 className={styles.section_title}>기본정보</h3>
            <div className={styles.table}>
              <div className={styles.table_tr}>
                <div className={styles.table_th}>감독</div>
                <div className={styles.table_td}>{movieDetail.directors}</div>
              </div>
              <div className={styles.table_tr}>
                <div className={styles.table_th}>출연진</div>
                <div className={styles.table_td}>{movieDetail.actors.join(',')}</div>
              </div>
              <div className={styles.table_tr}>
                <div className={styles.table_th}>러닝타임</div>
                <div className={styles.table_td}>{movieDetail.runningTime}분</div>
              </div>
            </div>
          </section>

          <section>
            <h3 className={styles.section_title}>줄거리</h3>
            <p className={styles.plot}>{movieDetail.plot}</p>
          </section>

          <section>
            <h3 className={styles.section_title}>예매하기</h3>
            <div className={styles.div__reservation}>
              <a href={movieDetail.reservationLink[0]} hidden={movieDetail.reservationLink[0] === null}>
                <img src={'/logo/megabox.png'} alt="메가박스 예매링크" />
              </a>
              <a href={movieDetail.reservationLink[1]} hidden={movieDetail.reservationLink[1] === null}>
                <img src={'/logo/cgv.png'} alt="cgv 예매링크" />
              </a>
              <a href={movieDetail.reservationLink[2]} hidden={movieDetail.reservationLink[2] === null}>
                <img src={'/logo/lotte.png'} alt="롯데시네마 예매링크" />
              </a>
            </div>
          </section>
        </div>
      </div>

      {recoMovies.length > 0 && (
        <div className={styles.div__recommend}>
          <span>구독자's Pick</span>
          <div className={styles.div__reco_list}>
            <div className={styles.div__movie_carousel}>
              {recoMovies.map((movie) => (
                <Link href={`/movie/${movie.movieId}`} key={`recommend_${movie.movieId}`}>
                  <img src={movie.posterBase64} />
                </Link>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
