import {configureStore, getDefaultMiddleware} from    "@reduxjs/toolkit";
import thunkMiddleware from 'redux-thunk';
import workplaceSlice from      "./workspaceSlice"
import boardSlice from          "./boardSlice"
import loginSlice from          "./loginSlice"
import otherUser from           "./otherUserSlice"

 const AllStore = configureStore({
    reducer:{
        workspace: workplaceSlice,
        board: boardSlice,
        login: loginSlice,
        otherUser: otherUser,

    }, 
    middleware: [...getDefaultMiddleware(), thunkMiddleware],
})
export default AllStore
