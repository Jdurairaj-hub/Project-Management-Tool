import {createSlice} from "@reduxjs/toolkit";


/*TODO using axios pull UserName data from other users to update initialState*/

const initialState = {
    data: ["Jimmy", "Timmy", "Finny", "Donny", "Lonnie", "Alejandro"]
}

const otherUserSlice = createSlice({
    name: 'otherUsers',
    initialState,

    reducers:{
        addPerson:(state, action) => {

            /*TODO Fix this logic so that:
            *                   Backend only returns users not part of workspace
            *                   User is only removed from list if they are added on backend (try/catch) */

            const delItem = state.data.indexOf(document.getElementById('currentUserVal').textContent)
            if (delItem !== -1){
                state.data.splice(delItem, 1);
            }

        }

    }



});
export const {addPerson,} = otherUserSlice.actions;
export default otherUserSlice.reducer;
