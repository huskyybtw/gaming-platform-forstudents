import { useNavigate } from 'react-router-dom';

function LoginPage(){
    const navigate = useNavigate();
    return (
        <button onClick={() => navigate("/profile")}> Login</button>
    );
}

export default LoginPage;