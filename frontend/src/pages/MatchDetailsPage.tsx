import React, { useEffect, useState } from "react";
import { useParams, useSearchParams } from "react-router-dom"; // Dodano useSearchParams
import axios from "axios";
import Cookies from "js-cookie";
import NavBar from "../components/NavBar.tsx";
import Footer from "../components/Footer.tsx";
import "../styles/PosterDetailsPage.css";

interface TeamUser {
    id: number;
    name: string;
}

interface MatchPosterDetails {
    id: string;
    title: string;
    description: string;
    ownerId: number;
    usersLeft: TeamUser[];
    usersRight: TeamUser[];
}

const PosterDetailsPage: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const [searchParams] = useSearchParams(); // Dodano searchParams
    const [matchPoster, setMatchPoster] = useState<MatchPosterDetails | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [loggedInUserId, setLoggedInUserId] = useState<number | null>(null);
    const [selectedTeam, setSelectedTeam] = useState<"left" | "right">("left");

    useEffect(() => {
        const userId = Cookies.get("userId");
        if (userId) {
            setLoggedInUserId(parseInt(userId, 10));
        }

        // Pobranie drużyny z parametru zapytania
        const teamParam = searchParams.get("team");
        if (teamParam === "100") {
            setSelectedTeam("left");
        } else if (teamParam === "200") {
            setSelectedTeam("right");
        }
    }, [searchParams]);

    const isUserInMatch = (poster: MatchPosterDetails) => {
        return [...poster.usersLeft, ...poster.usersRight].some(user => user.id === loggedInUserId);
    };

    const isUserOwner = (poster: MatchPosterDetails) => {
        return poster.ownerId === loggedInUserId;
    };

    useEffect(() => {
        const fetchPosterDetails = async () => {
            setIsLoading(true);
            try {
                const response = await axios.get(`${import.meta.env.VITE_BACKEND_URI}/api/v1/posters/match/${id}`);
                const data = response.data;

                console.log(data.participants); // Sprawdź, jak wyglądają uczestnicy

                setMatchPoster({
                    id: data.matchPoster.id,
                    title: `Match Poster ${data.matchPoster.id}`,
                    description: data.matchPoster.description,
                    ownerId: data.matchPoster.ownerId,
                    usersLeft: data.participants
                        .filter((user: any) => user.riot_team_number === 100)
                        .map((user: any) => ({
                            id: user.userId,
                            name: `User ${user.userId}`,
                        })),
                    usersRight: data.participants
                        .filter((user: any) => user.riot_team_number === 200)
                        .map((user: any) => ({
                            id: user.userId,
                            name: `User ${user.userId}`,
                        })),
                });


            } catch (err) {
                setError("Failed to load data. Please try again later.");
                console.error("Error loading poster details:", err);

            } finally {
                setIsLoading(false);
            }
        };

        fetchPosterDetails();
    }, [id]);

    const handleJoin = async (team: "left" | "right") => {
        if (!loggedInUserId) return;

        // Mapowanie "left" -> 100 i "right" -> 200get
        const teamCode = team === "left" ? 100 : 200;

        try {
            // Wysłanie zapytania z odpowiednimi parametrami
            await axios.post(`${import.meta.env.VITE_BACKEND_URI}/api/v1/posters/match/${id}/join/${loggedInUserId}`, null, {
                params: { team: teamCode }, // Parametr team w zapytaniu
            });
            window.location.reload(); // Odświeżenie strony
        } catch (err) {
            console.error(`Error while joining team ${team}:`, err);
            setError(`Failed to join team ${team === "left" ? "1" : "2"}.`);

        }
    };


    const handleLeave = async () => {
        if (!loggedInUserId) return;
        try {
            await axios.post(`${import.meta.env.VITE_BACKEND_URI}/api/v1/posters/match/${id}/leave/${loggedInUserId}`);
            window.location.reload();
        } catch (err) {
            console.error("Error while leaving the match:", err);
            setError("Failed to leave the match.");

        }
    };

    const handleRemoveUser = async (userId: number, team: "left" | "right") => {
        try {
            await axios.post(`${import.meta.env.VITE_BACKEND_URI}/api/v1/posters/match/${id}/leave/${userId}`);
            setMatchPoster((prev) => {
                if (!prev) return null;

                const updatedUsersLeft =
                    team === "left" ? prev.usersLeft.filter((user) => user.id !== userId) : prev.usersLeft;
                const updatedUsersRight =
                    team === "right" ? prev.usersRight.filter((user) => user.id !== userId) : prev.usersRight;

                return { ...prev, usersLeft: updatedUsersLeft, usersRight: updatedUsersRight };
            });
        } catch (err) {
            console.error("Error removing user:", err);
            setError("Failed to remove user.");

        }
    };

    // Sprawdzanie czy drużyna jest pełna
    const isTeamFull = (team: "left" | "right"): boolean => {
        if (!matchPoster) return false;
        return team === "left"
            ? matchPoster.usersLeft.length >= 5
            : matchPoster.usersRight.length >= 5;
    };

    if (isLoading) {
        return (
            <div className="container mt-4">
                <p>Loading...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="container mt-4">
                <p className="text-danger">{error}</p>
            </div>
        );
    }

    if (!matchPoster) {
        return (
            <div className="container mt-4">
                <p>Match not found.</p>
            </div>
        );
    }

    return (
        <div className="d-flex flex-column vh-100">
            <NavBar />

            <div className="container mt-4">
                <div className="mb-4">
                    <h1 className="text-primary">{matchPoster.title}</h1>
                    <p>{matchPoster.description}</p>

                    {loggedInUserId && !isUserOwner(matchPoster) && (
                        <div className="mb-3">
                            {!isUserInMatch(matchPoster) ? (
                                <div className="d-flex gap-2 align-items-center">
                                    <select
                                        className="form-select w-auto"
                                        value={selectedTeam}
                                        onChange={(e) => setSelectedTeam(e.target.value as "left" | "right")}
                                    >
                                        <option value="left" disabled={isTeamFull("left")}>
                                            Team {isTeamFull("left") ? "(Full)" : ""}
                                        </option>
                                        <option value="right" disabled={isTeamFull("right")}>
                                            Team 2 {isTeamFull("right") ? "(Full)" : ""}
                                        </option>
                                    </select>
                                    <button
                                        className="btn btn-success"
                                        onClick={() => handleJoin(selectedTeam)}
                                        disabled={isTeamFull(selectedTeam)}
                                    >
                                        Join Match
                                    </button>
                                </div>
                            ) : (
                                <button className="btn btn-warning" onClick={handleLeave}>
                                    Leave Match
                                </button>
                            )}
                        </div>
                    )}
                </div>

                <div className="row">
                    {/* Drużyna 1 */}
                    <div className="col-md-6">
                        <h2 className="font-weight-bold">Team 1</h2>
                        <div className="table-responsive">
                            <table className="table table-bordered table-striped table-hover">
                                <thead className="table-dark">
                                <tr>
                                    <th className="text-center">ID</th>
                                    <th className="text-center">Nick</th>
                                    {isUserOwner(matchPoster) && <th className="text-center">Action</th>}
                                </tr>
                                </thead>
                                <tbody>
                                {matchPoster.usersLeft.map((user) => (
                                    <tr key={user.id}>
                                        <td className="text-center">{user.id}</td>
                                        <td className="text-center">{user.name}</td>
                                        {isUserOwner(matchPoster) && (
                                            <td className="text-center">
                                                <button
                                                    className="btn btn-danger"
                                                    onClick={() => handleRemoveUser(user.id, "left")}
                                                >
                                                    Delete
                                                </button>
                                            </td>
                                        )}
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>

                    {/* Drużyna 2 */}
                    <div className="col-md-6">
                        <h2 className="font-weight-bold">Team 2</h2>
                        <div className="table-responsive">
                            <table className="table table-bordered table-striped table-hover">
                                <thead className="table-dark">
                                <tr>
                                    <th className="text-center">ID</th>
                                    <th className="text-center">Nick</th>
                                    {isUserOwner(matchPoster) && <th className="text-center">Action</th>}
                                </tr>
                                </thead>
                                <tbody>
                                {matchPoster.usersRight.map((user) => (
                                    <tr key={user.id}>
                                        <td className="text-center">{user.id}</td>
                                        <td className="text-center">{user.name}</td>
                                        {isUserOwner(matchPoster) && (
                                            <td className="text-center">
                                                <button
                                                    className="btn btn-danger"
                                                    onClick={() => handleRemoveUser(user.id, "right")}
                                                >
                                                    Delete
                                                </button>
                                            </td>
                                        )}
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <Footer />
        </div>
    );

};

export default PosterDetailsPage;