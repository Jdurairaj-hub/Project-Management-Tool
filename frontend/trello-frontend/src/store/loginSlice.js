/*
import {createSlice} from "@reduxjs/toolkit";

const initialState = {
    data: false
}

const loginSlice = createSlice({
    name: "login",
    initialState,
    data: false,
    reducers: {
        login:(state, )=>{
            state.data = true
            window.location.href = '/workspace';
        },
        logout:(state, )=>{
            state.data = false
            window.location.href = '/login';
        },

    }
});

export const {login, logout,} = loginSlice.actions;
export default loginSlice.reducer;
*/
import { createSlice } from "@reduxjs/toolkit";


const initialState = {
  data: {
    isLoggedIn: false,
    //email: null,
    //boardspaceData: null,
    //workspaceData: null
  },
};


const loginSlice = createSlice({
  name: "login",
  initialState,
  reducers: {
    login: (state, action) => {
        //console.log("This prints");
        //console.log(action.payload.userEmail);
      state.data = {
        isLoggedIn: true,
        
      };
      window.location.href = "/workspace";
    },
    logout: (state) => {
      state.data = {
        isLoggedIn: false,
        //email: null,
        //boardspaceData: null,
        //workspaceData: null
      };
      localStorage.clear();
      window.location.href = "/login";
    },
  },
});

export const { login, logout } = loginSlice.actions;
export default loginSlice.reducer;

