'use client';

import styles from '@/styles/components/performingArts/detail-tab.module.scss';
import { IPerformingArts } from '@/types';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { useState } from 'react';

export default function DetailTab({ performDetail, recoPerform }: { performDetail: IPerformingArts; recoPerform: IPerformingArts[] }) {
  const [tab, setTab] = useState<0 | 1>(0);
  const category = usePathname().split('/').slice(1)[1];

  const tabClickHandler = (e: React.MouseEvent<HTMLDivElement>) => {
    e.preventDefault();
    setTab((prev) => (prev ? 0 : 1));
  };

  return (
    <section className={styles.section__tab}>
      <div className={styles.div__btn_wrapper}>
        <div onClick={tabClickHandler} className={tab === 0 ? styles.selected : styles.not_selected}>
          기본 정보
        </div>
        <div onClick={tabClickHandler} className={tab === 1 ? styles.selected : styles.not_selected}>
          공연 소개
        </div>
      </div>
      <div className={styles.div__tab_content}>
        {!tab ? (
          <>
            <section className={styles.section}>
              <h3 className={styles.section_title}>위치·시간</h3>
              <div className={styles.table}>
                <div className={styles.table_tr}>
                  <div className={styles.table_th}>장소</div>
                  <div className={styles.table_td}>{`${performDetail.area}, ${performDetail.holeNm}`}</div>
                </div>
                <div className={styles.table_tr}>
                  <div className={styles.table_th}>공연시간</div>
                  <div className={styles.table_td}>{performDetail.runningTime}분</div>
                </div>
                <div className={styles.table_tr}>
                  <div className={styles.table_th}>시작시간</div>
                  <div className={styles.table_td}>{performDetail.dtguidance.join(',').replaceAll('), ', ') \n')}</div>
                </div>
              </div>
            </section>

            <section className={styles.section}>
              <h3 className={styles.section_title}>감독·출연진</h3>
              <div className={styles.table}>
                <div className={styles.table_tr}>
                  <div className={styles.table_th}>감독</div>
                  <div className={styles.table_td}>{performDetail.directors.join(',')}</div>
                </div>
                <div className={styles.table_tr}>
                  <div className={styles.table_th}>출연진</div>
                  <div className={styles.table_td}>{performDetail.actors.join(',')}</div>
                </div>
                <div className={styles.table_tr}>
                  <div className={styles.table_th}>회사</div>
                  <div className={styles.table_td}>{performDetail.companyNm.join(',')}</div>
                </div>
              </div>
            </section>

            <section className={styles.section}>
              <h3 className={styles.section_title}>예매하기</h3>
              <div className={styles.div__reservation}>
                {performDetail.relates.map((reservation, idx) => {
                  const [name, link] = reservation.split(' : ');

                  return (
                    <a key={`reservation_link_${idx}`} href={link}>
                      <div className={styles.div__link}>
                        <img src={'/images/tickets.png'} alt={`${name} 예매 링크`} />
                        <span>{name}</span>
                      </div>
                    </a>
                  );
                })}
              </div>
            </section>

            {recoPerform.length > 0 && (
              <div className={styles.div__recommend}>
                <span>구독자's Pick</span>
                <div className={styles.div__reco_list}>
                  <div className={styles.div__carousel}>
                    {recoPerform.map((perform) => (
                      <Link key={`recommend_${perform.id}`} href={`/performingArts/${category}/${perform.id}`}>
                        <img src={perform.poster} />
                      </Link>
                    ))}
                  </div>
                </div>
              </div>
            )}
          </>
        ) : (
          <div className={styles.div__styurls_wrapper}>
            {performDetail.styurls.map((imgPath, idx) => (
              <img src={imgPath} key={`${performDetail.name}의 공연 소개 이미지 ${idx + 1}`} />
            ))}
          </div>
        )}
      </div>
    </section>
  );
}
