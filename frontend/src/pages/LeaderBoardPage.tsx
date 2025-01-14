import { useEffect, useState } from "react";
import axios from "axios";
import NavBar from "../components/NavBar.tsx";
import SideBar from "../components/SideBar.tsx";
import Footer from "../components/Footer.tsx";
import { Link } from "react-router-dom";
import TeamsList from "../components/TeamsList.tsx";

interface Player {
    userId: number;
    nickname: string;
    tagLine: string;
    rating: number;
    profileIconId: number;
}

interface Team {
    id: number;
    teamName: string;
    rating: number;
}

function LeaderBoardPage() {
    const [players, setPlayers] = useState<Player[]>([]);
    const [teams, setTeams] = useState<Team[]>([]);
    const [displayTeams, setDisplayTeams] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchPlayers = async () => {
            try {
                const response = await axios.get<Player[]>(
                    `${import.meta.env.VITE_BACKEND_URI}/api/v1/rating/player/best`
                );
                setPlayers(response.data);
            } catch (err) {
                setError("Error fetching leaderboard data.");
            }
        };

        const fetchTeams = async () => {
            try {
                const response = await axios.get<Team[]>(
                    `${import.meta.env.VITE_BACKEND_URI}/api/v1/rating/team/best`
                );
                setTeams(response.data);
            } catch (err) {
                setError("Error fetching team leaderboard data.");
            }
        };

        fetchPlayers();
        fetchTeams();
    }, []);

    const toggleDisplay = () => {
        setDisplayTeams((prev) => !prev);
    };

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div className="d-flex flex-column vh-100">
            <NavBar />

            <div className="d-flex flex-grow-1">
                <main
                    className="d-flex flex-column flex-grow-1"
                    style={{ gridTemplateColumns: "1fr 1fr" }}
                >
                    <div className="d-flex justify-content-between align-items-center mb-3">
                        <h1>Leaderboards</h1>
                        <button className="btn btn-primary" onClick={toggleDisplay}>
                            {displayTeams ? "Show Best Players" : "Show Best Teams"}
                        </button>
                    </div>

                    {!displayTeams ? (
                        <ul className="list-group">
                            {players.map((player) => (
                                <li
                                    key={player.userId}
                                    className="list-group-item d-flex align-items-center mb-3"
                                    style={{ position: "relative" }}
                                >
                                    <Link
                                        to={`/profile/${player.userId}`}
                                        className="d-flex align-items-center text-decoration-none"
                                    >
                                        <img
                                            src={`https://ddragon.leagueoflegends.com/cdn/14.24.1/img/profileicon/${player.profileIconId}.png`}
                                            alt={`Profile Icon ${player.profileIconId}`}
                                            className="profile-icon me-3"
                                            style={{
                                                width: "30px",
                                                height: "30px",
                                                borderRadius: "50%",
                                            }}
                                        />
                                        <strong>{player.nickname}#{player.tagLine}</strong>
                                    </Link>
                                    <p className="ms-2 mb-0">Rating: {player.rating}</p>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <TeamsList teams={teams} />
                    )}
                </main>

                <aside
                    className="bg-light"
                    style={{ width: "200px", minWidth: "200px" }}
                >
                    <SideBar />
                </aside>
            </div>

            <Footer />
        </div>
    );
}

export default LeaderBoardPage;
