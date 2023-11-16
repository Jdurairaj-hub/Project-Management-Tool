import {TextField} from "@mui/material";
import Button from "@mui/material/Button";
import React, { useState } from 'react';
import {useFormik} from "formik";
import { useEffect } from 'react';
import * as yup from "yup"
import {useDispatch} from "react-redux";
import {addBoard} from "../store/boardSlice";
import axios from "axios";
import { Link, useNavigate } from 'react-router-dom';

export default function AddBoard(){

    const dispatch = useDispatch()
    const [email, setEmail] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
      console.log(localStorage.getItem("email"));
      if (localStorage.getItem("email") !== null) {
        setEmail(localStorage.getItem("email"));
      }
      else {
        navigate('/login');
      }
    }, []);

    const formik = useFormik({
        initialValues:{
            boardName: '',
        },
        validationSchema: yup.object({
            boardName: yup.string().min(1, "Board must have a name").required("Board must have a name")
        }),


        onSubmit: async (values, { setSubmitting, setErrors }) => {

            /*TODO  commented out the axios try and catch for add/remove functionality
            *       Ideally, should be in the slices so it can handle the frontend Registry  */

            //dispatch(addBoard(values.boardName));

            try {
            const response = await axios.post("http://localhost:8081/board/create?boardTitle=" + values.boardName + "&userEmail=" + localStorage.getItem("email"), {
              headers: {
                "Content-Type": "application/json",
              },
            });
            //dispatch(addBoard(values.boardName));
            //resetForm();
            if (response.status === 201) {
              dispatch(addBoard(values.boardName));
            } else {
              alert("An error occurred:", response.data);
              setErrors({ workspaceName: response.data});
            }
          } catch (error) {
            alert("An error occurred:", error);
            //setErrors({ workspaceName: "An error occurred. Please try again later." });
          } finally {
            setSubmitting(false);
          }
        },


      });
    const {getFieldProps,} = formik

    const inputLabelStyle = {
      color: "#dde4eb", // Replace with your desired text color
    };


    return(
        <>

            <form className="BoardInput"
                  onSubmit={formik.handleSubmit}
            >
                <TextField label="Create Board"
                           variant="standard"
                           onChange={formik.handleChange}
                           {...getFieldProps("boardName")}
                           InputLabelProps={{
                            style: inputLabelStyle,
                          }}
                />
                <Button variant="contained"
                        className="BoardButton"
                        type = "submit"

                >Submit </Button>
            </form>
        </>
    )

}