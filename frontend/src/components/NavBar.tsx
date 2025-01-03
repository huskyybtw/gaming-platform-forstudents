import Profile from "./Profile.tsx";

function navBar(){
    return <nav className="navbar navbar-expand-lg bg-body-tertiary">
        <div className="container-fluid">
            <button className="nav-item btn btn-dark">LeaderBoards</button>
            <button className="nav-item btn btn-dark">FindGames</button>
            <button className="nav-item btn btn-dark">FindPlayers</button>
            <Profile/>
        </div>
    </nav>
}

export default navBar;