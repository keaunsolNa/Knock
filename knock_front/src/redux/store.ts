import { configureStore } from '@reduxjs/toolkit';
import authReducer from './authSlice';
import modalReducer from './modalSlice';
import { useDispatch } from 'react-redux';

export const store = configureStore({
  reducer: {
    auth: authReducer,
    modal: modalReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export const useAppDispatch = () => useDispatch<typeof store.dispatch>();
