'use client';

import { MdModeEdit } from 'react-icons/md';
import styles from '@/styles/components/mypage/profile.module.scss';
import { IUser } from '@/types';
import { useState } from 'react';
import { apiRequest } from '@/utils/api';
import { useAppDispatch } from '@/redux/store';

const accountType = {
  GOOGLE: '구글 회원',
  KAKAO: ' 카카오톡 회원',
  NAVER: '네이버 회원',
  GUEST: '게스트 회원',
};

export default function Profile({ userData }: { userData: IUser }) {
  const dispatch = useAppDispatch();

  const [ogName, setOgName] = useState(userData.nickName);
  const [newName, setNewName] = useState(userData.nickName);
  const [isEdit, setIsEdit] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  /**
   * 수정 버튼 클릭 시
   * : 닉네임 수정 요청, 로딩 중 입력 막기, 에러시 span태그 보이게
   */
  const onClickSubmit = async (e: React.MouseEvent) => {
    if (isLoading) return;

    if (newName.length < 2) {
      setError('2글자 이상 작성해주세요');
      return;
    }

    setIsLoading(true);

    const response = await apiRequest(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/changeName`, dispatch, {
      method: 'POST',
      body: JSON.stringify({ value: newName }),
    });

    if (!response.ok) {
      setError('변경 실패. 잠시 후 다시 요청해주세요.');
      setIsLoading(false);
    }

    const nameChange = await response.json();

    if (nameChange) {
      setIsLoading(false);
      setIsEdit(false);
      setError('');
      setOgName(newName);
    }
  };

  /**
   * 취소버튼 클릭 시
   * : 수정중이던 이름 초기화, edit 모드 취소
   */
  const onClickCancel = () => {
    if (isLoading) return;

    setNewName(ogName);
    setIsEdit(false);
    setError('');
  };

  return (
    <>
      {userData && (
        <section className={styles.section__profile}>
          <img className={styles.img__profile} src={userData.picture} alt="유저 프로필 이미지" />
          {isEdit ? (
            <div className={styles.profile__editBox}>
              <input className={styles.input__nickname} value={newName} onChange={(e) => setNewName(e.target.value)} disabled={isLoading} />
              <p className={styles.p__error}>{error}</p>
              <div className={styles.div__btn_wrapper}>
                <div onClick={onClickSubmit}>수정</div>
                <div onClick={onClickCancel}>취소</div>
              </div>
            </div>
          ) : (
            <div className={styles.profile__textBox}>
              <div className={styles.div__nickname}>
                <span>{ogName}</span>
                <MdModeEdit onClick={() => setIsEdit(true)} />
              </div>
              <p className={styles.p__account}>{accountType[userData.loginType]}</p>
              <div className={styles.div__info}>
                {userData.loginType === 'GUEST' ? '회원가입하고 데이터를 연동해보세요' : '똑똑, 반가워요! 오늘도 Knock하세요'}
              </div>
            </div>
          )}
        </section>
      )}
    </>
  );
}
