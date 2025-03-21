import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface modalState {
  isOpen: boolean;
}

const initialState: modalState = {
  isOpen: false,
};

const modalSlice = createSlice({
  name: 'modal',
  initialState,
  reducers: {
    setModal: (state, action: PayloadAction<{ isOpen: boolean }>) => {
      state.isOpen = action.payload.isOpen;
    },
  },
});

export const { setModal } = modalSlice.actions;
export default modalSlice.reducer;
