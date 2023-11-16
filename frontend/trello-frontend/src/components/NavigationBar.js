import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import SettingsIcon from '@mui/icons-material/Settings';
/*&*importing React, Material UI, */

import axios from "axios";
import AddUserForm from './AddUserForm';
import "./style.css"
import Dropdown from "./WorkspaceDropdown"
import LogoutButton from "./LogoutButton"
import {FormControl, IconButton, InputLabel, MenuItem, Select, TextField} from "@mui/material";
import {useEffect, useState} from "react";
import AddWorkspace from "./AddWorkspace"
import WorkspaceDropdown from "./WorkspaceDropdown"
import { removeUserFromWorkspace } from "./WorkspaceDropdown"
import AddUser from "./AddUser"
import DeleteIcon from "@mui/icons-material/Delete";
import {useDispatch} from "react-redux";
import { setWorkspaceData } from '../store/workspaceSlice';

export default function NavigationBar() {
    const dispatch = useDispatch();

    const handleDeleteWorkspace = async () => {
        dispatch(removeUserFromWorkspace())
    }

    const removeUserFromWorkspace = async () => {
        try {
          const response = await axios.put(`http://localhost:8081/workspace/removeWorkspace?workspaceTitle=${localStorage.getItem("workspaceTitle")}&EmailID=${localStorage.getItem("email")}`);
          if (response.status === 200) {
            dispatch(setWorkspaceData(response.data));
            localStorage.setItem("workspaceData", JSON.stringify(response.data));
          } else {
            alert("Error occurred", response.data);
            console.log(response.data);
          }
        } catch (error) {
          alert("Error occurred: ", error);
          console.log(error);
        }
      };
    




        return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="static">
                <Toolbar className="toolBar">
                    <h1>Budget-Trello</h1>

                    <div className="dropdown" >
                        <Dropdown />

                    </div>

                    {/**Form function constantly checks whatever is being input to the field, only returns it when button is clicked*/}
                    <IconButton onClick={removeUserFromWorkspace}>
                        <DeleteIcon />
                    </IconButton>
                    <AddWorkspace/>





                    <div className="righthand">

                        <AddUserForm />
                        <LogoutButton />
                    </div>

                </Toolbar>
            </AppBar>
        </Box>
    );
}