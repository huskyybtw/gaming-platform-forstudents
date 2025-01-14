import { useNavigate } from 'react-router-dom';
import Cookies from "js-cookie";

function SideBar(){
    const navigate = useNavigate();

    const handleLogout = () => {
        Cookies.remove('token');
        Cookies.remove('userId');
        navigate('/');
    }
    return (
        <div className="d-flex flex-column vh-100 bg-light p-3">
            <ul>
                <li>My teams</li>
                <li>My Games</li>
            </ul>
            <button onClick={handleLogout} className="nav-item btn btn-dark">logout</button>
        </div >
    );
}

export default SideBar;