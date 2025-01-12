import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import '../styles/PlayerSearchBar.css';

interface Player {
    id: number;
    userId: number;
    nickname: string;
    tagLine: string;
    opgg: string | null;
    description: string;
    lastUpdate: string;
    rating: number;
    puuid: string;
    summonerid: string;
    accountId: string;
    profileIconId: number;
    summonerLevel: number;
}

function PlayerSearchBar() {
    const [players, setPlayers] = useState<Player[]>([]);
    const [searchTerm, setSearchTerm] = useState<string>("");
    const [suggestions, setSuggestions] = useState<Player[]>([]);
    const [showToast, setShowToast] = useState(false);
    const [toastMessage, setToastMessage] = useState("");
    const [toastType, setToastType] = useState<"success" | "danger">("success");
    const navigate = useNavigate();

    useEffect(() => {
        const fetchPlayers = async () => {
            try {
                const response = await axios.get<Player[]>(
                    `${import.meta.env.VITE_BACKEND_URI}/api/v1/players/`
                );
                setPlayers(response.data);
            } catch (err) {
                showToastNotification("Error fetching players data.", "danger");
            }
        };

        fetchPlayers();
    }, []);

    const handleSearch = () => {
        const matchedPlayer = players.find(
            (player) => player.nickname.toLowerCase() === searchTerm.trim().toLowerCase()
        );
        if (matchedPlayer) {
            navigate(`/profile/${matchedPlayer.userId}`);
        } else {
            showToastNotification("Player not found. Please check the nickname.", "danger");
        }
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setSearchTerm(value);

        if (value.trim() === "") {
            setSuggestions([]);
        } else {
            const filteredSuggestions = players.filter((player) =>
                player.nickname.toLowerCase().includes(value.toLowerCase())
            );
            setSuggestions(filteredSuggestions);
        }
    };

    const handleSuggestionClick = (player: Player) => {
        setSearchTerm(player.nickname);
        setSuggestions([]); // Clear suggestions after selecting one
    };

    const showToastNotification = (message: string, type: "success" | "danger") => {
        setToastMessage(message);
        setToastType(type);
        setShowToast(true);

        setTimeout(() => {
            setShowToast(false);
        }, 3000);
    };

    return (
        <div className="search-container">
            <div className="search-bar">
                <input
                    type="text"
                    placeholder="Search for a player..."
                    value={searchTerm}
                    onChange={handleInputChange}
                    className="search-input"
                />
                <button
                    onClick={handleSearch}
                    className="search-button"
                >
                    Search
                </button>
            </div>

            {suggestions.length > 0 && (
                <ul className="suggestions-list">
                    {suggestions.map((player) => (
                        <li
                            key={player.id}
                            onClick={() => handleSuggestionClick(player)}
                            className="suggestion-item"
                        >
                            {player.nickname}#{player.tagLine}
                        </li>
                    ))}
                </ul>
            )}

            {showToast && (
                <div
                    className={`toast align-items-center show ${
                        toastType === "success" ? "bg-success" : "bg-danger"
                    }`}
                    role="alert"
                    aria-live={toastType === "danger" ? "assertive" : "polite"}
                    aria-atomic="true"
                    style={{
                        position: "fixed",
                        bottom: "20px",
                        right: "20px",
                        zIndex: 9999,
                        transition: "opacity 0.5s ease-in-out",
                    }}
                >
                    <div className="d-flex">
                        <div className="toast-body">{toastMessage}</div>
                        <button
                            type="button"
                            className="btn-close me-2 m-auto"
                            aria-label="Close"
                            onClick={() => setShowToast(false)}
                        ></button>
                    </div>
                </div>
            )}
        </div>
    );
}

export default PlayerSearchBar;
