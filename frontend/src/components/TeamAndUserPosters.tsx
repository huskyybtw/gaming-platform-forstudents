import React, { useEffect, useState } from "react";
import axios from "axios";

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

    useEffect(() => {
        const fetchPosters = async () => {
            try {
                const teamResponse = await axios.get<TeamPoster[]>("http://localhost:8080/api/v1/posters/team/");
                const userResponse = await axios.get<UserPoster[]>("http://localhost:8080/api/v1/posters/user/");

                const formattedTeamPosters: TeamPoster[] = teamResponse.data.map((item: any) => ({
                    id: item.id,
                    teamId: item.teamId,
                    description: item.description,
                    dueDate: item.dueDate,
                    createdAt: item.createdAt,
                    updatedAt: item.updatedAt,
                    type: "TeamPoster",
                }));

                const formattedUserPosters: UserPoster[] = userResponse.data.map((item: any) => ({
                    id: item.id,
                    userId: item.userId,
                    description: item.description,
                    dueDate: item.dueDate,
                    createdAt: item.createdAt,
                    updatedAt: item.updatedAt,
                    type: "UserPoster",
                }));

                setTeamPosters(formattedTeamPosters);
                setUserPosters(formattedUserPosters);
            } catch (err) {
                setError("Nie udało się załadować plakatów. Spróbuj ponownie później.");
            }
        };

        fetchPosters();
    }, []);

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

    const handleSortChange = () => {
        setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    };

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
