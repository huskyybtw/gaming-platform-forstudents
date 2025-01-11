import { useNavigate } from 'react-router-dom';
import Cookies from "js-cookie";

function LoginPage(){
    const navigate = useNavigate();
    return (
        <button onClick={() => navigate(`/profile/${Cookies.get("userId")}`)}> Login</button>
    );
}

export default LoginPage;