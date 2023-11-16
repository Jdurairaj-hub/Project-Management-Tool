import React, { useState } from 'react';
import {TextField} from "@mui/material";
import Button from "@mui/material/Button";
import { Link, useNavigate } from 'react-router-dom';
import {useFormik} from "formik";
import { useEffect } from 'react';
import * as yup from "yup"
import {useDispatch, useSelector} from "react-redux";
import {addWorkspace} from "../store/workspaceSlice";
import {login} from "../store/loginSlice";
import axios from "axios";

const AddWorkspace = () => {

    const dispatch = useDispatch();
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
            workspaceName: '',
        },
        validationSchema: yup.object().shape({
          workspaceName: yup.string().when('isSubmitted', {
            // Enable validation only if the form is submitted
            is: true,
            then: yup.string().min(1, 'Workspace must have a name').required('Workspace must have a name'),
          }),
        }),

        onSubmit: async (values , { setSubmitting, setErrors }) => {

            /*TODO  commented out the axios try and catch for add/remove functionality
            *       Ideally, should be in the slices so it can handle the frontend Registry  */

            //dispatch(addWorkspace(values.workspaceName));


                try {
                  console.log(localStorage.getItem("email"))
                  console.log(values.workspaceName)
                  localStorage.setItem("workspaceTitle", values.workspaceName);
                  const response = await axios.post("http://localhost:8081/workspace/create?workspaceTitle=" + values.workspaceName + "&EmailID=" + localStorage.getItem("email"), {
                    headers: {
                      "Content-Type": "application/json",
                    },
                  });
                  
                  if (response.status === 201) {
                    dispatch(addWorkspace(values.workspaceName));
                  } else {
                    console.log(response)
                    alert("An error occurred:", response.data);
                    //setErrors({ workspaceName: response.data});
                  }
                } catch (error) {
                  alert("An error occurred. The workspace may already exist.", error);
                  console.log(error)
                  //setErrors({ workspaceName: "An error occurred. Please try again later." });
                } finally {
                  setSubmitting(false);
                }


              }


            });


    const {getFieldProps, handleSubmit, isSubmitting, touched, errors} = formik


    return(

        <form className="workspaceInput"
              id={"workspaceIntake"}
              onSubmit={formik.handleSubmit}
        >
            <TextField label="Create Workspace"
                       variant="standard"
                       onChange={formik.handleChange}
                       {...getFieldProps("workspaceName")}
            />
            <Button variant="contained"
                    type = "submit"
                    form={"workspaceIntake"}
                    className="workspaceButton"
            >Submit</Button>
            {formik.errors.workspaceName ? <p>{formik.errors.workspaceName}</p> : null}
        </form>

    );


    };
export default AddWorkspace;