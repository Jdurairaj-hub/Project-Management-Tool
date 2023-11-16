import {createSlice} from "@reduxjs/toolkit";
import axios from "axios";
const { localStorage } = window;

const initialState = {
    data: [],
}

const workspaceSlice = createSlice({
    name: "workplace",
    initialState,
    reducers: {
        addWorkspace:(state, action)=>{

            /*TODO ADD PROPER ERROR HANDLING TO THIS END
            *  *  Method only occurs when a unique Workspace is generated*/

            if (!state.data.includes(action.payload)){
                state.data.push(action.payload)
            }
            else{
                alert('Workspaces cannot have same names')
            }
            localStorage.setItem("workspaceData", JSON.stringify(state.data));

/*
            state.data.push(action.payload)*/
        },

        deleteWorkspace:(state, action) =>{
            /*Var meant to help cleanup this.that.this.that...*/
            const delItem = state.data.indexOf(document.getElementById('currentWorkspaceVal').textContent)
            if (delItem !== -1){
                state.data.splice(delItem, 1);
            }
            localStorage.setItem("workspaceData", JSON.stringify(state.data));
        }, 
        setWorkspaceData: (state, action) => {
            state.data = action.payload;
          localStorage.setItem("workspaceData", JSON.stringify(state.data))
        },
    }
});

export const addUserToWorkspace = async (email) => {
    try {
      const response = await axios.put(
        `http://localhost:8081/workspace/addWorkspace?workspaceTitle=`+localStorage.getItem("workspaceTitle")+`&EmailID=`+email
      );
      if (response.status === 200) {
        const workspaceData = await response.json();
        //dispatch(setWorkspaceData(workspaceData));
        localStorage.setItem('workspaceData', JSON.stringify(workspaceData));
      } else {
        alert('Error occurred', response.data);
        console.log(response.data);
      }
    } catch (error) {
      alert('Error occurred: ', error);
      console.log(error);
    }
  };

export const {addWorkspace, deleteWorkspace, setWorkspaceData} = workspaceSlice.actions;
export default workspaceSlice.reducer;
