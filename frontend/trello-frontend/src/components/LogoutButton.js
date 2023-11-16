import Button from "@mui/material/Button";
import { useDispatch} from 'react-redux'
import { logout } from "../store/loginSlice";

export default function LogoutButton(){

    const dispatch = useDispatch();



    const handleLogout = () => {
        dispatch(logout())
    };



    return(
        <>
            <Button color="inherit" onClick={handleLogout}>Logout</Button>
        </>
    )
}