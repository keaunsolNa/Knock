import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';

interface AuthState {
  accessToken: string | null;
  redirectUrl: string | null;
  isLoading: boolean;
  error: boolean;
}

const initialState: AuthState = {
  accessToken: null,
  redirectUrl: null,
  isLoading: false,
  error: false,
};

export const refreshAccessToken = createAsyncThunk('auth/refreshAccessToken', async (_, { rejectWithValue }) => {
  try {
    // Access 토큰 요청. Refresh Token 포함.
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/getAccessToken`, {
      method: 'POST',
      credentials: 'include',
    });

    // 401은 정상 케이스로 처리 (로그인 안된 상태)
    if (response.status === 401) {
      return { accessToken: null, redirectUrl: null };
    }

    // 401 외의 에러만 실제 에러로 처리
    if (!response.ok) {
      throw new Error(`${response.status} : ${response.statusText}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    // 네트워크 오류 등 실제 에러만 reject
    return rejectWithValue('네트워크 오류 발생');
  }
});

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setAuth: (state, action: PayloadAction<{ accessToken: string | null; redirectUrl: string | null }>) => {
      state.accessToken = action.payload.accessToken;
      state.redirectUrl = action.payload.redirectUrl;
    },
    clearAuth: (state) => {
      state.accessToken = null;
      state.redirectUrl = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(refreshAccessToken.pending, (state) => {
        state.isLoading = true;
        state.error = false;
      })
      .addCase(refreshAccessToken.fulfilled, (state, action) => {
        state.isLoading = false;
        state.accessToken = action.payload.accessToken;
        state.redirectUrl = action.payload.redirectUrl;
      })
      .addCase(refreshAccessToken.rejected, (state) => {
        state.isLoading = false;
        state.error = true;
      });
  },
});

export const { setAuth, clearAuth } = authSlice.actions;
export default authSlice.reducer;
