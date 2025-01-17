import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import NavBar from "../components/NavBar.tsx"; // Importujemy pasek nawigacyjny
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
    usersLeft: TeamUser[];
    usersRight: TeamUser[];
}

const PosterDetailsPage: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const [matchPoster, setMatchPoster] = useState<MatchPosterDetails | null>(null);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchPosterDetails = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/v1/posters/match/${id}`);
                const data = response.data;

                setMatchPoster({
                    id: data.matchPoster.id,
                    title: `Match Poster ${data.matchPoster.id}`,
                    description: data.matchPoster.description,
                    usersLeft: data.participants.slice(0, 5).map((user: any) => ({
                        id: user.userId,
                        name: `User ${user.userId}`,
                    })),
                    usersRight: data.participants.slice(5, 10).map((user: any) => ({
                        id: user.userId,
                        name: `User ${user.userId}`,
                    })),
                });
            } catch (err) {
                setError("Nie udało się załadować danych. Spróbuj ponownie później.");
                console.error("Błąd ładowania szczegółów plakatu:", err);
            }
        };

        fetchPosterDetails();
    }, [id]);

    const handleRemoveUser = async (userId: number, team: "left" | "right") => {
        try {
            await axios.delete(`http://localhost:8080/api/v1/posters/match/leave/${userId}`);
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
                <p>Ładowanie danych...</p>
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
                                    <th className="text-center">Akcje</th>
                                </tr>
                                </thead>
                                <tbody>
                                {matchPoster.usersLeft.map((user) => (
                                    <tr key={user.id}>
                                        <td className="text-center">{user.id}</td>
                                        <td className="text-center">{user.name}</td>
                                        <td className="text-center">
                                            <button
                                                className="btn btn-danger"
                                                onClick={() => handleRemoveUser(user.id, "left")}
                                            >
                                                Usuń
                                            </button>
                                        </td>
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
                                    <th className="text-center">Akcje</th>
                                </tr>
                                </thead>
                                <tbody>
                                {matchPoster.usersRight.map((user) => (
                                    <tr key={user.id}>
                                        <td className="text-center">{user.id}</td>
                                        <td className="text-center">{user.name}</td>
                                        <td className="text-center">
                                            <button
                                                className="btn btn-danger"
                                                onClick={() => handleRemoveUser(user.id, "right")}
                                            >
                                                Usuń
                                            </button>
                                        </td>
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
