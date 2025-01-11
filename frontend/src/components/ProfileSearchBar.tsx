import {useEffect, useState} from "react";
import axios from "axios";

function PlayerSearchBar(){
    const [players, setPlayers] = useState([]); // To store the fetched data
    const [error, setError] = useState(null); // To handle errors

    useEffect(() => {
        const fetchPlayers = async () => {
            try {
                // Make the API request
                const response = await axios.get(`${import.meta.env.VITE_BACKEND_URI}/api/v1/players/`);

                setPlayers(response.data);
            } catch (err) {
                setError(err.message || 'Something went wrong');
            }
        };

        fetchPlayers();
    }, []); // Empty dependency array means this effect runs once when the component mounts

    return (
        <div>
            <input type="text" placeholder="Search for a player..." />
            <button>Search</button>
        </div>
    );
}

export default PlayerSearchBar;