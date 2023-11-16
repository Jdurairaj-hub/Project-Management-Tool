import {FormControl, InputLabel, MenuItem, Select} from "@mui/material";
import {useSelector , useDispatch} from "react-redux";
import {useState, useEffect} from "react";
import { Link, useNavigate } from 'react-router-dom';
import {setCurrentWorkspaceTitle, removeUserFromWorkspace} from "../store/workspaceSlice";
//import React, { useState } from 'react';
import axios from "axios";


import { setWorkspaceData } from "../store/workspaceSlice";

export default function WorkspaceDropdown(){

    const [val, setVal] = useState('')
    const workspaceArr = useSelector(AllStore => AllStore.workspace.data)
    const dispatch = useDispatch();
    let email = "";
    const navigate = useNavigate();
    
    useEffect(() => {

      if (localStorage.getItem("email") !== null) {
        email = localStorage.getItem("email");
        fetchWorkspaceData();
      }
      else {
        navigate('/login');
      }

      const storedWorkspaceData = localStorage.getItem("workspaceData");

      if (storedWorkspaceData) {
        dispatch(setWorkspaceData(JSON.parse(storedWorkspaceData)));
      } else {
        fetchWorkspaceData();
      }
      
    }, []);

    function fetchWorkspaceData () {
      try {

        fetch ("http://localhost:8081/user/workspaces?userEmail=" + email)
        .then(response => response.json())
        .then (workspaceData => {
          dispatch(setWorkspaceData(workspaceData));
          localStorage.setItem("workspaceData", JSON.stringify(workspaceData));
        })

        // const response = await axios.get('http://localhost:8081/user/${'+email+'}/workspaces');
        // const workspaceData = response.data; 
        // dispatch(setWorkspaceData(workspaceData));
        // workspaceData.map(w => {
          // console.log(email);
        // });
        // dispatch(addWorkspace(values.workspaceName));
        // localStorage.setItem("workspaceData", JSON.stringify(workspaceData));
      } catch (error) {
        console.error("An error occurred:", error);
      }
    };

    const removeUserFromWorkspace = async () => {
      try {
        const response = await fetch(
          `http://localhost:8081/workspace/removeWorkspace?workspaceTitle=${localStorage.getItem("workspaceTitle")}&EmailID=${localStorage.getItem("email")}`
        );
        if (response.status === 200) {
          const workspaceData = await response.json();
          dispatch(setWorkspaceData(workspaceData));
          localStorage.setItem("workspaceData", JSON.stringify(workspaceData));
        } else {
              alert("Error occured",  response.data)
              console.log(response.data)
          }
      } catch (error) {
          alert("Error occured : ", error)
          console.log(error)
      }
    };
    



    return(

        <FormControl sx={{ m: 1, minWidth: 40 }} fullWidth>
            <InputLabel>Workspace</InputLabel>
            <Select value ={val}
                    key={workspaceArr.indexOf(val)}
                    onChange = {e=>setVal(e.target.value)}
                    id={'currentWorkspaceVal'}
            >
                {workspaceArr.map((opt) =>(<MenuItem key={opt.workspaceID} value={opt.workspaceTitle} >{opt.workspaceTitle}</MenuItem>))}
            </Select>
        </FormControl>
    )

}