import { ReactNode } from 'react';
import styles from './layout.module.scss';

function AboutKnock() {
  return (
    <section className={styles.section__about}>
      <p className={styles.span__about}>
        <span className={styles.span__category}>영화, 공연예술</span>
        을 <br />
        사랑하는 당신을 위한
        <span className={styles.span__point}> 티켓팅 헬퍼</span>
      </p>

      <div className={styles.div__logo}>
        <span className={styles.span__point}>K</span>
        <span>NOCK</span>
      </div>
    </section>
  );
}

export default function Layout({ children }: { children: ReactNode }) {
  return (
    <>
      <div className={styles.container}>
        <AboutKnock />
        {children}
      </div>
    </>
  );
}
