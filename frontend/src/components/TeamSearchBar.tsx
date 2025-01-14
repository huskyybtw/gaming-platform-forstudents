import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

interface Team {
    id: number;
    teamName: string;
    description: string;
    teamCaptain: number;
    rating: number;
}

interface TeamResponse {
    team: Team;
    users: number[];
}

function TeamSearchBar() {
    const [teams, setTeams] = useState<Team[]>([]);
    const [searchTerm, setSearchTerm] = useState<string>("");
    const [suggestions, setSuggestions] = useState<Team[]>([]);
    const [showToast, setShowToast] = useState(false);
    const [toastMessage, setToastMessage] = useState("");
    const [toastType, setToastType] = useState<"success" | "danger">("success");
    const navigate = useNavigate();

    // Fetch teams data
    useEffect(() => {
        const fetchTeams = async () => {
            try {
                const response = await axios.get<TeamResponse[]>(
                    `${import.meta.env.VITE_BACKEND_URI}/api/v1/teams/`
                );
                // Extract teams from the response data
                setTeams(response.data.map((item) => item.team));
                console.log("Fetched teams:", response.data);
            } catch (err) {
                showToastNotification("Error fetching teams data.", "danger");
                console.error("Error fetching teams:", err);
            }
        };

        fetchTeams();
    }, []);

    // Handle search button click
    const handleSearch = () => {
        const matchedTeam = teams.find((team) =>
            team.teamName.toLowerCase().includes(searchTerm.trim().toLowerCase())
        );
        if (matchedTeam) {
            navigate(`/teams/${matchedTeam.id}`);
        } else {
            showToastNotification("Team not found. Please check the name.", "danger");
        }
    };

    // Handle input change and update suggestions
    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setSearchTerm(value);

        if (value.trim() === "") {
            setSuggestions([]);
        } else {
            const filteredSuggestions = teams.filter((team) =>
                team.teamName.toLowerCase().includes(value.toLowerCase())
            );
            setSuggestions(filteredSuggestions);
        }
    };

    // Display toast notification
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
                    placeholder="Search for a team..."
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
                    {suggestions.map((team) => (
                        <li
                            key={team.id}
                            onClick={() => {
                                setSearchTerm(team.teamName);
                                setSuggestions([]);
                            }}
                            className="suggestion-item"
                        >
                            {team.teamName}
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

export default TeamSearchBar;
