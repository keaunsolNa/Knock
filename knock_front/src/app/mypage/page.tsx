import styles from './page.module.scss';
import { MdModeEdit } from 'react-icons/md';
import MenuItem from '@/components/mypage/MenuItem';

export default function Page() {
  const account = 'guest';

  return (
    <div className={styles.container}>
      <section className={styles.section__profile}>
        <img
          className={styles.img__profile}
          src={'/images/user_profile_default.png'}
          alt="유저 프로필 이미지"
        />
        <div className={styles.profile__textbox}>
          <div className={styles.div__nickname}>
            <span>Guest 12345</span>
            <MdModeEdit />
          </div>
          <p className={styles.p__account}>게스트 회원</p>
          <div className={styles.div__info}>
            {account === 'guest'
              ? '회원가입하고 데이터를 연동해보세요'
              : '똑똑, 반가워요! 오늘도 Knock하세요'}
          </div>
        </div>
      </section>

      <section className={styles.section__subscribe}>
        <h2>📌 나의 구독</h2>
        <div className={styles.div__menu_box}>
          <MenuItem name="카테고리" link="/mypage/category" value="영화" />
          <MenuItem name="구독 목록" link="/mypage/subscribe" />
        </div>
      </section>
      <section className={styles.section__alarm}>
        <h2>⏰ 알림 설정</h2>
        <div className={styles.div__menu_box}>
          <MenuItem name="영화" link="/mypage/alarm/movie" value={'1시간 전'} />
          <MenuItem
            name="뮤지컬"
            link="/mypage/alarm/musical"
            value={'1시간 전'}
          />
          <MenuItem
            name="오페라"
            link="/mypage/alarm/opera"
            value={'1시간 전'}
          />
          <MenuItem
            name="전시회"
            link="/mypage/alarm/exhibition"
            value={'없음'}
          />
        </div>
      </section>

      {account === 'guest' ? (
        <section className={styles.section__account}>
          <h2>🔗 계정 연동</h2>
          <p>
            데이터가 기기에만 저장되어 있습니다. 앱 삭제, 기기 변동, 예기치 않은
            오류 발생 시 데이터가 손실될 가능성이 있습니다.
            <br /> <br />
            간단하게 계정 연동을 하고, 보다 안전하게 데이터를 보호하세요!
          </p>
          <div className={styles.div__login_wrapper}>
            <button className={styles.btn} />
            <button className={styles.btn} />
            <button className={styles.btn} />
          </div>
        </section>
      ) : (
        <button className={styles.btn__logout}>로그아웃</button>
      )}
    </div>
  );
}
