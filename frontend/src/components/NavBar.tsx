import { useNavigate } from 'react-router-dom';

function NavBar(){
    const navigate = useNavigate();
    return <nav className="navbar navbar-expand-lg bg-body-tertiary">
        <div className="container-fluid">
            <button onClick={() => navigate('/leaderboards')} className="nav-item btn btn-dark">LeaderBoards</button>
            <button onClick={() => navigate('/games')} className="nav-item btn btn-dark">FindGames</button>
            <button onClick={() => navigate('/players')} className="nav-item btn btn-dark">FindPlayers</button>
            <button onClick={() => navigate('/profile')} type="button" className="nav-item btn btn-dark">Profile</button>
        </div>
    </nav>
}

export default NavBar;