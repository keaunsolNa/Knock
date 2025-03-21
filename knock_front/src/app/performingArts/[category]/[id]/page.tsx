import styles from './page.module.scss';
import Image from 'next/image';
import SubscribeBtn from '@/components/SubscribeBtn';
import Tag from '@/components/Tag';
import { prfStateToString } from '@/utils/typeToText';
import { IPerformingArts } from '@/types';
import DetailTab from '@/components/performingArts/DetailTab';

export default async function Page({ params }: { params: Promise<{ id: string }> }) {
  const { id } = await params;
  const [performResponse, recoResponse] = await Promise.all([
    fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/performingArts/getDetail?performingArtsId=${id}`),
    fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/performingArts/recommend?performingArtsId=${id}`),
  ]);

  if (!performResponse.ok || !recoResponse.ok) {
    throw new Error(`performingArts/${id} SSR 페이지 API 요청 실패`);
  }

  const performDetail: IPerformingArts = await performResponse.json();
  const recoPerform: IPerformingArts[] = await recoResponse.json();

  const dateFormat = (date: Date) => {
    date = new Date(date);

    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');

    return `${year}.${month}.${day}`;
  };

  return (
    <div>
      <div className={styles.img__cover_container} style={{ backgroundImage: `url('${performDetail.poster}')` }}>
        <Image
          className={styles.img__poster}
          src={performDetail.poster ?? '/images/noImage.png'}
          alt={performDetail.poster ? `${performDetail.name} 포스터` : `${performDetail.name} 포스터 대체 이미지`}
          width={300}
          height={400}
          priority
        />
        <SubscribeBtn favorites={performDetail.favoritesCount} id={performDetail.id} />
      </div>

      <div className={styles.div__details}>
        <section className={styles.main_info__wrapper}>
          <div className={styles.div__category_list}>
            <Tag text={prfStateToString[performDetail.prfState]} />
          </div>
          <h2 className={styles.title}>{performDetail.name}</h2>
          <h3 className={styles.open_date}>
            {performDetail.from === performDetail.to
              ? `${dateFormat(performDetail.from)}`
              : `${dateFormat(performDetail.from)} ~ ${dateFormat(performDetail.to)}`}
          </h3>
        </section>

        <DetailTab performDetail={performDetail} recoPerform={recoPerform} />
      </div>
    </div>
  );
}
