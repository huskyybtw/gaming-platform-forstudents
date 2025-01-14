import React, { useEffect, useState } from "react";
import axios from "axios";
import Cookies from "js-cookie";

const token = Cookies.get("token");
console.log(token); // Powinien wyświetlić Twój token

interface TeamPoster {
    id: number;
    teamId: number;
    description: string;
    dueDate: string;
    createdAt: string;
    updatedAt: string;
    type: "TeamPoster";
}

interface UserPoster {
    id: number;
    userId: number;
    description: string;
    dueDate: string;
    createdAt: string;
    updatedAt: string;
    type: "UserPoster";
}

const TeamAndUserPosters: React.FC = () => {
    const [teamPosters, setTeamPosters] = useState<TeamPoster[]>([]);
    const [userPosters, setUserPosters] = useState<UserPoster[]>([]);
    const [searchQuery, setSearchQuery] = useState<string>("");
    const [sortOrder, setSortOrder] = useState<"asc" | "desc">("asc");
    const [error, setError] = useState<string>("");
    const [newPosterType, setNewPosterType] = useState<"TeamPoster" | "UserPoster">("TeamPoster");
    const [newPoster, setNewPoster] = useState<{ teamId?: number; userId?: number; description: string; dueDate: string }>({
        description: "",
        dueDate: "",
    });
    const [isFormVisible, setIsFormVisible] = useState<boolean>(false);

    useEffect(() => {
        const fetchPosters = async () => {
            try {
                const token = Cookies.get("token");
                if (!token) {
                    setError("Brak tokenu autoryzacyjnego.");
                    return;
                }

                const teamResponse = await axios.get<TeamPoster[]>(
                    "http://localhost:8080/api/v1/posters/team/",
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    }
                );

                const userResponse = await axios.get<UserPoster[]>(
                    "http://localhost:8080/api/v1/posters/user/",
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    }
                );

                setTeamPosters(teamResponse.data);
                setUserPosters(userResponse.data);
            } catch (err) {
                setError("Nie udało się załadować plakatów. Spróbuj ponownie później.");
            }
        };

        fetchPosters();
    }, []);


    const handleSortChange = () => {
        setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    };

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
            const response = await axios.post(
                `http://localhost:8080/api/v1/posters${endpoint}`,
                newPoster,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            if (newPosterType === "TeamPoster") {
                setTeamPosters((prev) => [...prev, response.data]);
            } else {
                setUserPosters((prev) => [...prev, response.data]);
            }

            setNewPoster({ description: "", dueDate: "" });
            setIsFormVisible(false);
        } catch (err) {
            setError(`Nie udało się dodać ${newPosterType === "TeamPoster" ? "plakatu zespołu" : "plakatu użytkownika"}.`);
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
        <div>
            {error && <p className="error-message">{error}</p>}

            <div className="search-bar">
                <input
                    type="text"
                    placeholder="Szukaj plakatów..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="form-control"
                />
            </div>

            <div className="sort-bar">
                <button onClick={handleSortChange} className="btn btn-secondary">
                    Sortuj według terminu: {sortOrder === "asc" ? "Rosnąco" : "Malejąco"}
                </button>
            </div>

            <h2>Dodaj Poster</h2>
            <button onClick={() => setIsFormVisible(!isFormVisible)} className="btn btn-primary">
                {isFormVisible ? "Anuluj" : "Dodaj Nowy Poster"}
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
                        <input
                            type="number"
                            name="userId"
                            placeholder="User ID"
                            value={newPoster.userId || ""}
                            onChange={handleInputChange}
                            className="form-control"
                        />
                    )}
                    <textarea
                        name="description"
                        placeholder="Opis plakatu"
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
                    <button onClick={handleAddPoster} className="btn btn-success">
                        Dodaj Plakat
                    </button>
                </div>
            )}

            <h2>Team Posters</h2>
            <div className="posters-container">
                {sortedTeamPosters.map((poster) => (
                    <div key={poster.id} className="poster-card">
                        <h3>Team {poster.teamId}</h3>
                        <p>{poster.description}</p>
                        <p>Termin zgłoszeń: {new Date(poster.dueDate).toLocaleDateString()}</p>
                        <p>Data utworzenia: {new Date(poster.createdAt).toLocaleDateString()}</p>
                    </div>
                ))}
                {sortedTeamPosters.length === 0 && <p>Brak plakatów dla zespołów.</p>}
            </div>

            <h2>User Posters</h2>
            <div className="posters-container">
                {sortedUserPosters.map((poster) => (
                    <div key={poster.id} className="poster-card">
                        <h3>User {poster.userId}</h3>
                        <p>{poster.description}</p>
                        <p>Termin zgłoszeń: {new Date(poster.dueDate).toLocaleDateString()}</p>
                        <p>Data utworzenia: {new Date(poster.createdAt).toLocaleDateString()}</p>
                    </div>
                ))}
                {sortedUserPosters.length === 0 && <p>Brak plakatów użytkowników.</p>}
            </div>
        </div>
    );
};

export default TeamAndUserPosters;
