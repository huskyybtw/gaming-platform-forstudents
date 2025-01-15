import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import NavBar from "../components/NavBar.tsx";  // Importujemy pasek nawigacyjny

import Footer from "../components/Footer.tsx";
import '../styles/PosterDetailsPage.css';

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
            {/* Pasek nawigacyjny */}
            <NavBar />

            <div className="container mt-4">
                {/* Informacje o plakacie */}
                <div className="mb-4">
                    <h1 className="text-primary">{matchPoster.title}</h1>
                    <p>{matchPoster.description}</p>
                </div>

                {/* Layout 2 tabelki */}
                <div className="row">
                    {/* Lewa tabelka - drużyna 1 */}
                    <div className="col-md-6">
                        <h2 className="font-weight-bold">Drużyna 1</h2>
                        <div className="table-responsive">
                            <table className="table table-bordered table-striped table-hover">
                                <thead className="table-dark">
                                <tr>
                                    <th className="text-center">ID</th>
                                    <th className="text-center">Nick</th>
                                </tr>
                                </thead>
                                <tbody>
                                {matchPoster.usersLeft.map((user) => (
                                    <tr key={user.id}>
                                        <td className="text-center">{user.id}</td>
                                        <td className="text-center">{user.name}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>

                    {/* Prawa tabelka - drużyna 2 */}
                    <div className="col-md-6">
                        <h2 className="font-weight-bold">Drużyna 2</h2>
                        <div className="table-responsive">
                            <table className="table table-bordered table-striped table-hover">
                                <thead className="table-dark">
                                <tr>
                                    <th className="text-center">ID</th>
                                    <th className="text-center">Nick</th>
                                </tr>
                                </thead>
                                <tbody>
                                {matchPoster.usersRight.map((user) => (
                                    <tr key={user.id}>
                                        <td className="text-center">{user.id}</td>
                                        <td className="text-center">{user.name}</td>
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
