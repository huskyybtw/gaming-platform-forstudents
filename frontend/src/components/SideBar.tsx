import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import axios from "axios";

function SideBar() {
    const navigate = useNavigate();
    const [matchPosters, setMatchPosters] = useState<any[]>([]);
    const [teams, setTeams] = useState<any[]>([]);
    const [serverStatus, setServerStatus] = useState({ color: "gray", label: "Checking..." });

    const handleLogout = () => {
        Cookies.remove("token");
        Cookies.remove("userId");
        navigate("/");
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                // Fetch match posters
                const postersResponse = await axios.get(
                    `${import.meta.env.VITE_BACKEND_URI}/api/v1/posters/match/`
                );
                const userId = parseInt(Cookies.get("userId") || "0", 10);
                const filteredPosters = postersResponse.data.filter((poster: any) =>
                    poster.participants.some((participant: any) => participant.userId === userId)
                );
                setMatchPosters(filteredPosters);

                // Fetch teams
                const teamsResponse = await axios.get(`${import.meta.env.VITE_BACKEND_URI}/api/v1/teams/`);
                const filteredTeams = teamsResponse.data.filter((team: any) =>
                    team.users.includes(userId)
                );
                setTeams(filteredTeams);
            } catch (err) {
                console.error("Error fetching data:", err);
            }
        };

        const checkServerStatus = async () => {
            try {
                const start = performance.now();
                await axios.get(`${import.meta.env.VITE_BACKEND_URI}/api/v1/teams/`);
                const duration = performance.now() - start;

                if (duration < 200) {
                    setServerStatus({ color: "green", label: "Good" });
                } else if (duration < 500) {
                    setServerStatus({ color: "yellow", label: "Moderate" });
                } else {
                    setServerStatus({ color: "red", label: "Poor" });
                }
            } catch {
                setServerStatus({ color: "red", label: "Offline" });
            }
        };

        fetchData();
        checkServerStatus();

        // Periodically check server status
        const interval = setInterval(checkServerStatus, 60000); // Check every 60 seconds
        return () => clearInterval(interval);
    }, [navigate]);

    return (
        <div className="d-flex flex-column vh-100 bg-light p-3">
            {/* Server Status */}
            <div className="mb-3 d-flex align-items-center">
                <div
                    style={{
                        width: "12px",
                        height: "12px",
                        borderRadius: "50%",
                        backgroundColor: serverStatus.color,
                        marginRight: "8px",
                    }}
                ></div>
                <span>Server: {serverStatus.label}</span>
            </div>

            {/* Teams Section */}
            <h5>Your Teams</h5>
            <ul className="list-unstyled mb-3">
                {teams.map((team: any) => (
                    <li key={team.id}>
                        <button
                            className="btn btn-primary w-100 mb-2 text-start"
                            style={{
                                fontWeight: "500",
                                padding: "10px",
                                borderRadius: "8px",
                            }}
                            onClick={() => navigate(`/teams/${team.id}`)}
                        >
                            {team.team.teamName}
                        </button>
                    </li>
                ))}
            </ul>

            {/* Match Posters Section */}
            <h5>Match Posters</h5>
            <ul className="list-unstyled mb-3">
                {matchPosters.map((poster) => (
                    <li key={poster.matchPoster.id}>
                        <button
                            className="btn btn-outline-secondary w-100 mb-2 text-start"
                            style={{
                                fontWeight: "500",
                                padding: "10px",
                                borderRadius: "8px",
                            }}
                            onClick={() => navigate(`/games/${poster.matchPoster.id}`)}
                        >
                            Game ID: {poster.matchPoster.id}, Players: {poster.participants.length}/10, Due:{" "}
                            {new Date(poster.matchPoster.dueDate).toLocaleDateString()}
                        </button>
                    </li>
                ))}
            </ul>

            {/* Logout Button */}
            <div className="mt-4">
                <button
                    onClick={handleLogout}
                    className="btn btn-dark w-100"
                    style={{
                        padding: "10px",
                        borderRadius: "8px",
                        fontWeight: "600",
                    }}
                >
                    Logout
                </button>
            </div>
        </div>
    );
}

export default SideBar;
