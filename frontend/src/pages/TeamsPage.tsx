import { useEffect, useState } from "react";
import axios from "axios";
import Cookies from "js-cookie";
import NavBar from "../components/NavBar.tsx";
import SideBar from "../components/SideBar.tsx";
import Footer from "../components/Footer.tsx";
import TeamSearchBar from "../components/TeamSearchBar.tsx";
import { Link } from "react-router-dom";

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
                const response = await axios.get<TeamData[]>(
                    `${import.meta.env.VITE_BACKEND_URI}/api/v1/teams/`
                );
                setTeams(response.data);
            } catch (err) {
                console.error("Error fetching teams:", err);
            }
        };

        fetchTeams();
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setNewTeamData((prev) => ({
            ...prev,
            [name]: value,
        }));
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
                Authorization: `Bearer ${Cookies.get("token")}`, // Token from cookies
            };

            const response = await axios.post(
                `${import.meta.env.VITE_BACKEND_URI}/api/v1/teams/`,
                requestBody,
                { headers }
            );

            // Handle success
            setTeams((prev) => [...prev, response.data]); // Add the new team to the list
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
        <div className="d-flex flex-column vh-100">
            <NavBar />
            <TeamSearchBar />
            <div className="d-flex flex-grow-1">
                <main
                    className="d-grid flex-grow-1"
                    style={{
                        gridTemplateColumns: "1fr 1fr",
                    }}
                >
                    <h1>Teams</h1>
                    <div>
                        <button
                            className="btn btn-primary"
                            onClick={() => setIsCreating(true)}
                        >
                            Create Team
                        </button>
                    </div>
                    <ul className="list-group">
                        {teams.map((teamData) => (
                            <li
                                key={teamData.team.id}
                                className="list-group-item d-flex flex-column mb-3"
                            >
                                <div className="d-flex justify-content-between align-items-center">
                                    <strong>{teamData.team.teamName}</strong>
                                    <span>Rating: {teamData.team.rating}</span>
                                </div>
                                <p className="mb-1">{teamData.team.description}</p>
                                <Link
                                    to={`/teams/${teamData.team.id}`}
                                    className="text-decoration-none"
                                >
                                    View Details
                                </Link>
                            </li>
                        ))}
                    </ul>
                </main>

                <aside
                    className="bg-light"
                    style={{ width: "200px", minWidth: "200px" }}
                >
                    <SideBar />
                </aside>
            </div>

            <Footer />

            {isCreating && (
                <div className="modal-backdrop">
                    <div className="modal-container">
                        <button
                            type="button"
                            onClick={closeModal}
                            className="close-button"
                            aria-label="Close"
                        >
                            &times;
                        </button>
                        <h2>Create New Team</h2>
                        <form onSubmit={handleSubmit}>
                            <div className="mb-3">
                                <label htmlFor="teamName" className="form-label">
                                    Team Name
                                </label>
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
                                <label htmlFor="description" className="form-label">
                                    Description
                                </label>
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
                                <button type="submit" className="btn btn-primary">
                                    Submit
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}

export default TeamsPage;
