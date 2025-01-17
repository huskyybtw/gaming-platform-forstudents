import { useNavigate } from "react-router-dom";
import NavBar from "../components/NavBar.tsx";

function AccessForbiddenPage() {
    const navigate = useNavigate();

    const handleBackToLogin = () => {
        navigate("/");
    };

    return (
        <div>
            <NavBar />
            <div>
                <h1>Access Forbidden</h1>
                <button onClick={handleBackToLogin}>Try Login</button>
            </div>
        </div>
    );
}

export default AccessForbiddenPage;
