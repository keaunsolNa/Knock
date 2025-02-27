import { refreshAccessToken } from '@/redux/authSlice';
import { store, AppDispatch } from '@/redux/store';

/**
 * 백엔드 api 통신 시, Access Token이 자동으로 추가.
 */
export const apiRequest = async (
  url: string,
  dispatch: AppDispatch,
  options: RequestInit = {}
) => {
  let accessToken = store.getState().auth.accessToken;
  let response = await fetch(url, {
    ...options, // method ,body 등과 같은 정보 입력

    // Access 토큰 추가 , header option 추가
    headers: {
      'Content-Type': 'application/json',
      Authorization: `${accessToken}`,
    },
  });

  // Access Token 만료
  if (response.status === 401) {
    dispatch(refreshAccessToken()); // Access token Refresh 요청
    accessToken = store.getState().auth.accessToken;
    if (!accessToken) return response; // Refresh Token도 만료 : 요청페이지에서 logout 진행

    // 재시도
    response = await fetch(url, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${accessToken}`,
      },
    });
  }

  return response;
};
