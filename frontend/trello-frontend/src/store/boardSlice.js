import {createSlice} from "@reduxjs/toolkit";
import dropdown from "../components/BoardDropdown"
import {useSelector} from "react-redux";
const { localStorage } = window;


const initialState = {
    data: []
}


const boardSlice = createSlice({
    name: "board",
    initialState,
    reducers: {
        addBoard:(state, action)=>{

            /*TODO ADD PROPER ERROR HANDLING TO THIS END
            *  Method only occurs when a unique BoardName is generated*/

            if (!state.data.includes(action.payload)){
                state.data.push(action.payload)
            }
            else{
                alert('Boards cannot have same names')
            }
            localStorage.setItem("boardspaceData", JSON.stringify(state.data));
        },


        deleteBoard: (state, action) => {

            /*Var meant to help cleanup this.that.this.that...*/


            const delItem = state.data.indexOf(document.getElementById('currentBoardVal').textContent)

            if (delItem !== -1){
                state.data.splice(delItem, 1);
            }
            localStorage.setItem("boardspaceData", JSON.stringify(state.data));
          },
          setBoardspaceData: (state, action) => {
            state.data = action.payload;
          localStorage.setItem("boardspaceData", JSON.stringify(state.data))
        },
    },
});

export const {addBoard, deleteBoard, setBoardspaceData } = boardSlice.actions
export default boardSlice.reducer
