import { useNavigate } from "react-router-dom";

function AccessForbiddenPage() {
    const navigate = useNavigate();

    const handleBackToLogin = () => {
        navigate("/");
    };

    return (
        <div>
            <h1>Access Forbidden</h1>
            <button onClick={handleBackToLogin}>Back To Login</button>
        </div>
    );
}

export default AccessForbiddenPage;
