import { useNavigate } from 'react-router-dom';

function SideBar(){
    const navigate = useNavigate();
    return (
        <div className="d-flex flex-column vh-100 bg-light p-3">
            <ul>
                <li>Lorem ipsum</li>
                <li>Lorem ipsum</li>
            </ul>
            <button onClick={() => navigate('/')} className="nav-item btn btn-dark">logout</button>
        </div >
    );
}

export default SideBar;