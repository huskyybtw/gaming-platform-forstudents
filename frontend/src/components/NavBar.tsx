import { useNavigate } from 'react-router-dom';
import Cookies from "js-cookie";

function NavBar(){
    const navigate = useNavigate();
    return <nav className="navbar navbar-expand-lg bg-body-tertiary">
        <div className="container-fluid">
            <button onClick={() => navigate('/')} className="nav-item btn btn-dark">LeaderBoards</button>
            <button onClick={() => navigate('/games')} className="nav-item btn btn-dark">FindGames</button>
            <button onClick={() => navigate(`/players`)} className="nav-item btn btn-dark">Look For Players</button>
            <button onClick={() => navigate('/teams')} className="nav-item btn btn-dark">Teams</button>
            <button
                onClick={() => navigate(`/profile/${Cookies.get("userId")}`)}
                className="nav-item btn btn-dark"
            >
                Profile
            </button>
        </div>
    </nav>
}

export default NavBar;