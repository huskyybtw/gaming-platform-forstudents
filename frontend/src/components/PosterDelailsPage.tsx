import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import Cookies from "js-cookie";
import NavBar from "../components/NavBar.tsx";
import Footer from "../components/Footer.tsx";
import "../styles/PosterDetailsPage.css";

interface TeamUser {
    id: number;
    name: string;
    frag: number;
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
    const [matchPoster, setMatchPoster] = useState<MatchPosterDetails | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [loggedInUserId, setLoggedInUserId] = useState<number | null>(null);

    useEffect(() => {
        const userId = Cookies.get("userId");
        if (userId) {
            setLoggedInUserId(parseInt(userId, 10));
        }
    }, []);

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

                setMatchPoster({
                    id: data.matchPoster.id,
                    title: `Match Poster ${data.matchPoster.id}`,
                    description: data.matchPoster.description,
                    ownerId: data.matchPoster.ownerId,
                    usersLeft: data.participants.slice(0, 5).map((user: any) => ({
                        id: user.userId,
                        name: `User ${user.userId}`,
                        frag: user.frag || 0
                    })),
                    usersRight: data.participants.slice(5, 10).map((user: any) => ({
                        id: user.userId,
                        name: `User ${user.userId}`,
                        frag: user.frag || 0
                    })),
                });
            } catch (err) {
                setError("Nie udało się załadować danych. Spróbuj ponownie później.");
                console.error("Błąd ładowania szczegółów plakatu:", err);
            } finally {
                setIsLoading(false);
            }
        };

        fetchPosterDetails();
    }, [id]);

    const handleJoin = async () => {
        if (!loggedInUserId) return;
        try {
            await axios.post(`${import.meta.env.VITE_BACKEND_URI}/api/v1/posters/match/${id}/join/${loggedInUserId}`);
            window.location.reload();
        } catch (err) {
            console.error("Błąd podczas dołączania do meczu:", err);
            setError("Nie udało się dołączyć do meczu.");
        }
    };

    const handleLeave = async () => {
        if (!loggedInUserId) return;
        try {
            await axios.post(`${import.meta.env.VITE_BACKEND_URI}/api/v1/posters/match/${id}/leave/${loggedInUserId}`);
            window.location.reload();
        } catch (err) {
            console.error("Błąd podczas opuszczania meczu:", err);
            setError("Nie udało się opuścić meczu.");
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
            console.error("Błąd usuwania użytkownika:", err);
            setError("Nie udało się usunąć użytkownika.");
        }
    };

    if (isLoading) {
        return (
            <div className="container mt-4">
                <p>Ładowanie danych...</p>
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
                <p>Nie znaleziono meczu.</p>
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
                                <button className="btn btn-success" onClick={handleJoin}>
                                    Dołącz do meczu
                                </button>
                            ) : (
                                <button className="btn btn-warning" onClick={handleLeave}>
                                    Opuść mecz
                                </button>
                            )}
                        </div>
                    )}
                </div>

                <div className="row">
                    <div className="col-md-6">
                        <h2 className="font-weight-bold">Drużyna 1</h2>
                        <div className="table-responsive">
                            <table className="table table-bordered table-striped table-hover">
                                <thead className="table-dark">
                                <tr>
                                    <th className="text-center">ID</th>
                                    <th className="text-center">Nick</th>
                                    <th className="text-center">Frag</th>
                                    {isUserOwner(matchPoster) && <th className="text-center">Akcje</th>}
                                </tr>
                                </thead>
                                <tbody>
                                {matchPoster.usersLeft.map((user) => (
                                    <tr key={user.id}>
                                        <td className="text-center">{user.id}</td>
                                        <td className="text-center">{user.name}</td>
                                        <td className="text-center">{user.frag}</td>
                                        {isUserOwner(matchPoster) && (
                                            <td className="text-center">
                                                <button
                                                    className="btn btn-danger"
                                                    onClick={() => handleRemoveUser(user.id, "left")}
                                                >
                                                    Usuń
                                                </button>
                                            </td>
                                        )}
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div className="col-md-6">
                        <h2 className="font-weight-bold">Drużyna 2</h2>
                        <div className="table-responsive">
                            <table className="table table-bordered table-striped table-hover">
                                <thead className="table-dark">
                                <tr>
                                    <th className="text-center">ID</th>
                                    <th className="text-center">Nick</th>
                                    <th className="text-center">Frag</th>
                                    {isUserOwner(matchPoster) && <th className="text-center">Akcje</th>}
                                </tr>
                                </thead>
                                <tbody>
                                {matchPoster.usersRight.map((user) => (
                                    <tr key={user.id}>
                                        <td className="text-center">{user.id}</td>
                                        <td className="text-center">{user.name}</td>
                                        <td className="text-center">{user.frag}</td>
                                        {isUserOwner(matchPoster) && (
                                            <td className="text-center">
                                                <button
                                                    className="btn btn-danger"
                                                    onClick={() => handleRemoveUser(user.id, "right")}
                                                >
                                                    Usuń
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