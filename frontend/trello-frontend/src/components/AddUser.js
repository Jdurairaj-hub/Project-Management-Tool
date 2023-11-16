import {FormControl, InputLabel, MenuItem, Select, TextField} from "@mui/material";
import Button from "@mui/material/Button";
import {useFormik} from "formik";
import * as yup from "yup";
import axios from "axios";
import {useDispatch, useSelector} from "react-redux";
import {useState} from "react";
import {addPerson} from "../store/otherUserSlice";


export default function AddUser(){

    const [val, setVal] = useState('')
    const otherUsers = useSelector((state) => state.otherUser.data);
    const dispatch = useDispatch()

    const formik = useFormik({
        initialValues:{
            username: '',
        },

        /*validationSchema: yup.object({
            /!*ToDo can add error handling to check whether the user you are adding exists
            *  or Make a formik dropdown showing all users and submit that*!/
            username: yup.string().min(1, "Enter valid Username").required("Enter valid Username")

        }),*/

        onSubmit: async (values) =>
        {
            dispatch(addPerson())


/*
            try {
              const response = await axios.post("http://localhost:8081/user/addUser", {
                username: values.username,
              });
              console.log(response.data);
            } catch (error) {
              console.error("An error occurred:", error);
            }
*/
          },
        });


    const {getFieldProps,} = formik


    return(
        <>

            <form className="AddUser"
                  onSubmit={formik.handleSubmit}
            >
                <Select className="userField"
                        value ={val}
                        key={otherUsers.indexOf(val)}
                        onChange = {e=>setVal(e.target.value)}
                        id={'currentUserVal'}
                >
                    {otherUsers.map((opt) => (<MenuItem key={opt} value={opt}>{opt}</MenuItem>))}
                </Select>

                <Button variant="contained"
                        className="Add User"
                        type = "submit"
                >Add User </Button>
            </form>
        </>
    )
}