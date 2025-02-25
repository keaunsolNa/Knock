'use client';

import { MdModeEdit } from 'react-icons/md';
import styles from '@/styles/components/mypage/profile.module.scss';
import { IUser } from '@/types';
import { useState } from 'react';
import { apiRequest } from '@/utils/api';
import { useAppDispatch } from '@/redux/store';

export default function Profile({ userData }: { userData: IUser }) {
  const dispatch = useAppDispatch();

  const [ogName, setOgName] = useState(userData.nickName);
  const [newName, setNewName] = useState(userData.nickName);
  const [isEdit, setIsEdit] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [isError, setIsError] = useState(false);

  const accountType = () => {
    switch (userData.loginType) {
      case 'GOOGLE':
        return '구글 회원';
      case 'KAKAO':
        return '카카오톡 회원';
      case 'NAVER':
        return '네이버 회원';
      case 'GUEST':
        return '게스트 회원';
    }
  };

  /**
   * 수정 버튼 클릭 시
   * : 닉네임 수정 요청, 로딩 중 입력 막기, 에러시 span태그 보이게
   */
  const onClickSubmit = async (e: React.MouseEvent) => {
    setIsLoading(true);

    const response = await apiRequest(
      `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/user/changeName`,
      dispatch,
      {
        method: 'POST',
        body: JSON.stringify({ value: newName }),
      }
    );

    if (!response.ok) {
      setIsError(true);
      setIsLoading(false);
    }

    const nameChange = await response.json();

    if (nameChange) {
      setIsLoading(false);
      setIsEdit(false);
      setOgName(newName);
    }
  };

  /**
   * 취소버튼 클릭 시
   * : 수정중이던 이름 초기화, edit 모드 취소
   */
  const onClickCancel = () => {
    setNewName(ogName);
    setIsEdit(false);
  };

  return (
    <>
      {userData && (
        <section className={styles.section__profile}>
          <img
            className={styles.img__profile}
            src={userData.picture}
            alt="유저 프로필 이미지"
          />
          <div className={styles.profile__textbox}>
            {isEdit ? (
              <>
                <input
                  className={styles.input__nickname}
                  value={newName}
                  onChange={(e) => setNewName(e.target.value)}
                  disabled={isLoading}
                />
                <div className={styles.div__btn_wrapper}>
                  <button disabled={isLoading} onClick={onClickSubmit}>
                    수정
                  </button>
                  <button disabled={isLoading} onClick={onClickCancel}>
                    취소
                  </button>
                </div>
                {isError && (
                  <p className={styles.p__error}>
                    변경 실패. 잠시 후 다시 요청해주세요.
                  </p>
                )}
              </>
            ) : (
              <>
                <div className={styles.div__nickname}>
                  <span>{ogName}</span>
                  <MdModeEdit onClick={() => setIsEdit(true)} />
                </div>
                <p className={styles.p__account}>{accountType()}</p>
                <div className={styles.div__info}>
                  {userData.loginType === 'GUEST'
                    ? '회원가입하고 데이터를 연동해보세요'
                    : '똑똑, 반가워요! 오늘도 Knock하세요'}
                </div>
              </>
            )}
          </div>
        </section>
      )}
    </>
  );
}
