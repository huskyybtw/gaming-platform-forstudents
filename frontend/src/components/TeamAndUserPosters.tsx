import React, { useEffect, useState } from "react";
import axios from "axios";
import Cookies from "js-cookie";
import "../styles/FindPlayersPage.css";
import {useNavigate} from "react-router-dom";

interface TeamPoster {
    id: number;
    teamId: number;
    description: string;
    dueDate: string;
    createdAt: string;
}

interface UserPoster {
    id: number;
    userId: number;
    description: string;
    dueDate: string;
    createdAt: string;
}

const TeamAndUserPosters: React.FC = () => {
    const [teamPosters, setTeamPosters] = useState<TeamPoster[]>([]);
    const [userPosters, setUserPosters] = useState<UserPoster[]>([]);
    const [searchQuery, setSearchQuery] = useState<string>("");
    const [sortOrder, setSortOrder] = useState<"asc" | "desc">("asc");
    const [error, setError] = useState<string>("");
    const [newPosterType, setNewPosterType] = useState<"TeamPoster" | "UserPoster">("TeamPoster");
    const [newPoster, setNewPoster] = useState<{ teamId?: number; userId?: number;description: string; dueDate: string }>({
        description: "",
        dueDate: "",
    });
    const [isFormVisible, setIsFormVisible] = useState<boolean>(false);
    const userId = Cookies.get("userId"); // Pobieranie userId z ciasteczka
    const navigate = useNavigate();
    useEffect(() => {
        const fetchPosters = async () => {
            try {
                const token = Cookies.get("token");
                if (!token) {
                    navigate("/forbidden");
                }

                const teamResponse = await axios.get<TeamPoster[]>("http://localhost:8080/api/v1/posters/team/", {
                    headers: { Authorization: `Bearer ${token}` },
                });

                const userResponse = await axios.get<UserPoster[]>("http://localhost:8080/api/v1/posters/user/", {
                    headers: { Authorization: `Bearer ${token}` },
                });

                setTeamPosters(teamResponse.data);
                setUserPosters(userResponse.data);
            } catch (err) {
                setError(`Nie udało się załadować plakatów. Spróbuj ponownie później ${err}.`,);
            }
        };

        fetchPosters();
    }, []);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setNewPoster((prev) => ({ ...prev, [name]: value }));
    };

    const handlePosterTypeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setNewPosterType(e.target.value as "TeamPoster" | "UserPoster");
    };

    const handleAddPoster = async () => {
        try {
            const token = Cookies.get("token");
            if (!token) {
                setError("Brak tokenu autoryzacyjnego.");
                return;
            }

            const endpoint = newPosterType === "TeamPoster" ? "/team/" : "/user/";
            const payload = { ...newPoster };

            if (newPosterType === "UserPoster" && userId) {
                payload["userId"] = parseInt(userId, 10);
            }

            const response = await axios.post(`http://localhost:8080/api/v1/posters${endpoint}`, payload, {
                headers: { Authorization: `Bearer ${token}` },
            });

            if (newPosterType === "TeamPoster") {
                setTeamPosters((prev) => [...prev, response.data]);
            } else {
                setUserPosters((prev) => [...prev, response.data]);
            }

            setNewPoster({ description: "", dueDate: "" });
            setIsFormVisible(false);
            window.location.reload();
        } catch (err) {
            setError(`Nie udało się dodać ${newPosterType === "TeamPoster" ? "plakatu zespołu" : "plakatu użytkownika" }${err}`);
        }
    };

    const handleDeletePoster = async (posterId: number, type: "TeamPoster" | "UserPoster") => {
        try {
            const token = Cookies.get("token");
            if (!token) {
                setError("Brak tokenu autoryzacyjnego.");
                return;
            }

            const endpoint = type === "TeamPoster" ? "/team/" : "/user/";
            await axios.delete(`http://localhost:8080/api/v1/posters${endpoint}${posterId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            if (type === "TeamPoster") {
                setTeamPosters((prev) => prev.filter((poster) => poster.id !== posterId));
            } else {
                setUserPosters((prev) => prev.filter((poster) => poster.id !== posterId));
            }
            window.location.reload();
        } catch (err) {
            setError(`Nie udało się usunąć plakatu. Spróbuj ponownie później${err}.`);
        }
    };

    const filteredTeamPosters = teamPosters.filter((poster) =>
        poster.description.toLowerCase().includes(searchQuery.toLowerCase())
    );

    const filteredUserPosters = userPosters.filter((poster) =>
        poster.description.toLowerCase().includes(searchQuery.toLowerCase())
    );

    const sortedTeamPosters = filteredTeamPosters.sort((a, b) => {
        const dateA = new Date(a.dueDate).getTime();
        const dateB = new Date(b.dueDate).getTime();
        return sortOrder === "asc" ? dateA - dateB : dateB - dateA;
    });

    const sortedUserPosters = filteredUserPosters.sort((a, b) => {
        const dateA = new Date(a.dueDate).getTime();
        const dateB = new Date(b.dueDate).getTime();
        return sortOrder === "asc" ? dateA - dateB : dateB - dateA;
    });

    return (
        <div className="container">
            {error && <p className="alert alert-danger">{error}</p>}

            <div className="search-sort-bar">
                <input
                    type="text"
                    placeholder="Search Posters..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="form-control search-input"
                />
                <button
                    onClick={() => setSortOrder(sortOrder === "asc" ? "desc" : "asc")}
                    className="btn btn-secondary sort-button"
                >
                    Sort: {sortOrder === "asc" ? "Ascending" : "Descending"}
                </button>
            </div>

            <div className="add-poster-section">
                <h2>Add Poster</h2>
                <button onClick={() => setIsFormVisible(!isFormVisible)} className="btn btn-primary toggle-form-button">
                    {isFormVisible ? "Cancel" : "Add New Poster"}
                </button>
                {isFormVisible && (
                    <div className="add-poster-form">
                        <select onChange={handlePosterTypeChange} value={newPosterType} className="form-control">
                            <option value="TeamPoster">Team Poster</option>
                            <option value="UserPoster">User Poster</option>
                        </select>
                        {newPosterType === "TeamPoster" && (
                            <input
                                type="number"
                                name="teamId"
                                placeholder="Team ID"
                                value={newPoster.teamId || ""}
                                onChange={handleInputChange}
                                className="form-control"
                            />
                        )}
                        {newPosterType === "UserPoster" && (
                            <p>User ID: {userId || "No user data in cookie."}</p>
                        )}
                        <textarea
                            name="description"
                            placeholder="Poster Description"
                            value={newPoster.description}
                            onChange={handleInputChange}
                            className="form-control"
                        />
                        <input
                            type="date"
                            name="dueDate"
                            value={newPoster.dueDate}
                            onChange={handleInputChange}
                            className="form-control"
                        />
                        <button onClick={handleAddPoster} className="btn btn-success add-poster-button">
                            Add Poster
                        </button>
                    </div>
                )}
            </div>

            <div className="posters-section">
                <h2>Team Posters</h2>
                <div className="posters-container">
                    {sortedTeamPosters.length > 0 ? (
                        sortedTeamPosters.map((poster) => (
                            <div key={poster.id} className="card poster-card">
                                <h3>Team {poster.teamId}</h3>
                                <p>{poster.description}</p>
                                <p>Due Date: {new Date(poster.dueDate).toLocaleDateString()}</p>
                                <p>Created: {new Date(poster.createdAt).toLocaleDateString()}</p>
                                <button
                                    onClick={() => handleDeletePoster(poster.teamId, "TeamPoster")}
                                    className="btn btn-danger delete-button"
                                >
                                    Delete
                                </button>
                            </div>
                        ))
                    ) : (
                        <p>No team posters.</p>
                    )}
                </div>

                <h2>User Posters</h2>
                <div className="posters-container">
                    {sortedUserPosters.length > 0 ? (
                        sortedUserPosters.map((poster) => (
                            <div key={poster.id} className="card poster-card">
                                <h3>User {poster.userId}</h3>
                                <p>{poster.description}</p>
                                <p>Due Date: {new Date(poster.dueDate).toLocaleDateString()}</p>
                                <p>Created: {new Date(poster.createdAt).toLocaleDateString()}</p>
                                <button
                                    onClick={() => handleDeletePoster(poster.userId, "UserPoster")}
                                    className="btn btn-danger delete-button"
                                >
                                    Delete
                                </button>
                            </div>
                        ))
                    ) : (
                        <p>No user posters.</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default TeamAndUserPosters;
