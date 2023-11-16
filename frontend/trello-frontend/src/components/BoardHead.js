import Dropdown from "./BoardDropdown"
import * as React from 'react';
import DeleteIcon from '@mui/icons-material/Delete';
import {IconButton, TextField} from "@mui/material";
import Button from "@mui/material/Button";
import AddBoard from "./AddBoard"
import { useDispatch } from 'react-redux';
import { deleteBoard } from '../store/boardSlice';
import axios from 'axios';

/* TODO
    const handleDeleteBoard = async () => {
    try {
        // Make an HTTP DELETE request to the deleteBoard endpoint
        const response = await axios.delete(`/api/boards/${boardId}`);
        console.log(response.data); 
      } catch (error) {
        console.error('An error occurred:', error);
        // Handle the error if needed
      }
      };
  */

export default function BoardHead({ boardId }){
    const dispatch = useDispatch();

    const handleDeleteBoard = async () => {

        dispatch(deleteBoard())


        /*TODO  commented out the axios try and catch for add/remove functionality
            *       Ideally, should be in the slices so it can handle the frontend Registry  */
        /*try {
          const response = await axios.delete(`/api/board/${boardId}/delete`);
          dispatch(deleteBoard(boardId));
        } catch (error) {
          console.error('An error occurred:', error);
        }*/


    }
    
    return(

        <body className="boardHead">
            <h2>CurrentBoard</h2>
            <Dropdown />

            <IconButton onClick={handleDeleteBoard}>
                <DeleteIcon />
            </IconButton>
        
            <AddBoard />

        </body>
    )


}