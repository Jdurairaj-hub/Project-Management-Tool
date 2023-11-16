import {FormControl, InputLabel, MenuItem, Select, ThemeProvider} from "@mui/material";
import { createTheme } from "@mui/material";
import {useSelector, useDispatch} from "react-redux";
import {useState, useEffect} from "react";
import axios from "axios";
import { Link, useNavigate } from 'react-router-dom';

import { setBoardspaceData } from "../store/boardSlice";

const api = "";

export default function BoardDropdown(){

    const [val, setVal] = useState('')
    const boardArr = useSelector((state) => state.board.data);
    const dispatch = useDispatch();
    const [email, setEmail] = useState('');
    const navigate = useNavigate();

    useEffect(() => {

      const storedBoardspaceData = localStorage.getItem("boardspaceData");

      if (storedBoardspaceData) {
        dispatch(setBoardspaceData(JSON.parse(storedBoardspaceData)));
        } else {
        fetchWorkspaceData();
        }
      if (localStorage.getItem("email") !== null) {
        setEmail(localStorage.getItem("email"));
      }
      else {
        navigate('/login');
      }
    }, []);

    //const handleChange = async (event) => {
      async function fetchWorkspaceData () {
        try {
          /*
          const selectedBoard = event.target.value;
          const response = await axios.post("http://localhost:8081/user/" + email + "/boards", { selectedBoard });
          console.log(response.data);
          setVal(selectedBoard);
          */
          const response = await axios.get('http://localhost:8081/user/${'+email+'}/boards');
          const boardspaceData = response.data; 
          dispatch(setBoardspaceData(boardspaceData));
          localStorage.setItem("boardspaceData", JSON.stringify(boardspaceData));
        } catch (error) {
          console.error("An error occurred:", error);
        }


    };

    
    
    return(
      
        <FormControl sx={{ m: 1, minWidth: 40 }} fullWidth>
            <InputLabel sx={{ color: "#dde4eb" }}>Board</InputLabel>
            <Select value ={val}
                    key={boardArr.indexOf(val)}
                    onChange = {e=>setVal(e.target.value)}
                    id={'currentBoardVal'}
            >
                {boardArr.map((opt) => (<MenuItem key={opt} value={opt}>{opt}</MenuItem>))}
            </Select>
        </FormControl>
      
    )

}