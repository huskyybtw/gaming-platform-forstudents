import { useEffect, useState } from "react";
import axios from "axios";
import Cookies from "js-cookie";
import NavBar from "../components/NavBar.tsx";
import SideBar from "../components/SideBar.tsx";
import Footer from "../components/Footer.tsx";
import TeamSearchBar from "../components/TeamSearchBar.tsx";
import { Link } from "react-router-dom";
import '../styles/TeamPage.css';

interface TeamData {
    team: {
        id: number;
        teamName: string;
        description: string;
        teamCaptain: number;
        rating: number;
    };
    users: number[];
}

function TeamsPage() {
    const [teams, setTeams] = useState<TeamData[]>([]);
    const [isCreating, setIsCreating] = useState(false);
    const [newTeamData, setNewTeamData] = useState({ teamName: "", description: "" });
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchTeams = async () => {
            try {
                const response = await axios.get<TeamData[]>(`${import.meta.env.VITE_BACKEND_URI}/api/v1/teams/`);
                setTeams(response.data);
            } catch (err) {
                console.error("Error fetching teams:", err);
            }
        };
        fetchTeams();
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setNewTeamData((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const requestBody = {
            teamName: newTeamData.teamName,
            description: newTeamData.description,
            teamCaptain: Cookies.get("userId"), // User ID from cookies
            rating: 1000,
        };

        try {
            const headers = {
                "Content-Type": "application/json",
                Authorization: `Bearer ${Cookies.get("token")}`,
            };

            const response = await axios.post(
                `${import.meta.env.VITE_BACKEND_URI}/api/v1/teams/`,
                requestBody,
                { headers }
            );

            setTeams((prev) => [...prev, response.data]); // Add new team
            closeModal();
        } catch (err: any) {
            console.error("Error creating team:", err);
            setError("Failed to create team. Please try again.");
        }
    };

    const closeModal = () => {
        setIsCreating(false);
        setNewTeamData({ teamName: "", description: "" });
        setError(null);
    };

    return (
        <div className="page-container">
            <NavBar />
            <div className="page-main">
                <main style={{ flexGrow: 1 }}>
                    <h1 className="page-header">Teams</h1>
                    <TeamSearchBar />
                    <div className="create-button-container">
                        <button className="create-team-button" onClick={() => setIsCreating(true)}>
                            Create Team
                        </button>
                    </div>

                    <ul className="team-list">
                        {teams.map((teamData) => (
                            <li key={teamData.team.id} className="team-item">
                                <div className="team-header">
                                    <strong>{teamData.team.teamName}</strong>
                                    <span className="team-rating">Rating: {teamData.team.rating}</span>
                                </div>
                                <p className="team-description">{teamData.team.description}</p>
                                <Link to={`/teams/${teamData.team.id}`} className="view-details-link">
                                    View Details
                                </Link>
                            </li>
                        ))}
                    </ul>
                </main>

                <aside className="bg-light" style={{ width: "200px", minWidth: "200px" }}>
                    <SideBar />
                </aside>
            </div>

            <Footer />

            {isCreating && (
                <div className="modal-backdrop">
                    <div className="modal-container">
                        <button type="button" onClick={closeModal} className="close-button" aria-label="Close">
                            &times;
                        </button>
                        <h2>Create New Team</h2>
                        <form onSubmit={handleSubmit}>
                            <div className="mb-3">
                                <label htmlFor="teamName" className="form-label">Team Name</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    id="teamName"
                                    name="teamName"
                                    value={newTeamData.teamName}
                                    onChange={handleChange}
                                    placeholder="Enter team name"
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="description" className="form-label">Description</label>
                                <textarea
                                    className="form-control"
                                    id="description"
                                    name="description"
                                    value={newTeamData.description}
                                    onChange={handleChange}
                                    placeholder="Enter team description"
                                    rows={3}
                                    required
                                ></textarea>
                            </div>
                            {error && <p className="text-danger">{error}</p>}
                            <div>
                                <button type="submit" className="submit-button">Submit</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}

export default TeamsPage;
