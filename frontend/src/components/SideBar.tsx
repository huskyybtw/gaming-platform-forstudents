import { useNavigate } from 'react-router-dom';

function SideBar(){
    const navigate = useNavigate();
    return (
        <div className="d-flex flex-column vh-100 bg-light p-3">
            <ul>
                <li>My teams</li>
                <li>My Games</li>
            </ul>
            <button onClick={() => navigate('/')} className="nav-item btn btn-dark">logout</button>
        </div >
    );
}

export default SideBar;