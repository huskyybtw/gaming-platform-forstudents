import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../styles/MainPage.css';
import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";

interface MatchPoster {
    id: number;
    title: string;
    description: string;
    dueDate: string;
    createdAt: string;
    updatedAt: string;
    type: 'MatchPoster';
}

const MainPage: React.FC = () => {
    const [matchPosters, setMatchPosters] = useState<MatchPoster[]>([]);
    const [searchQuery, setSearchQuery] = useState<string>('');
    const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('asc');
    const [error, setError] = useState<string>('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchMatchPosters = async () => {
            try {
                const token = Cookies.get('token'); // Pobranie tokenu z cookies
                const response = await axios.get<MatchPoster[]>('http://localhost:8080/api/v1/posters/match/', {
                    headers: {
                        Authorization: `Bearer ${token}`, // Dodanie tokenu do nagłówków
                    },
                });

                const formattedPosters: MatchPoster[] = response.data.map((item: any) => ({
                    id: item.matchPoster.id,
                    title: `Match ${item.matchPoster.id}`,
                    description: item.matchPoster.description,
                    dueDate: item.matchPoster.dueDate,
                    createdAt: item.matchPoster.createdAt,
                    updatedAt: item.matchPoster.updatedAt,
                    type: 'MatchPoster',
                }));
                setMatchPosters(formattedPosters);
            } catch (err) {
                setError('Nie udało się załadować plakatów. Spróbuj ponownie później.');
            }
        };

        fetchMatchPosters();
    }, []);

    // Filtrowanie na podstawie wyszukiwania
    const filteredPosters = matchPosters.filter((poster) =>
        poster.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
        poster.description.toLowerCase().includes(searchQuery.toLowerCase())
    );

    // Sortowanie według daty
    const sortedPosters = filteredPosters.sort((a, b) => {
        const dateA = new Date(a.dueDate).getTime();
        const dateB = new Date(b.dueDate).getTime();
        return sortOrder === 'asc' ? dateA - dateB : dateB - dateA;
    });

    // Obsługa zmiany sortowania
    const handleSortChange = () => {
        setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc');
    };

    // Funkcja usuwania plakatu
    const handleDeletePoster = async (posterId: number) => {
        try {
            const token = Cookies.get('token');
            if (!token) {
                setError('Brak tokenu autoryzacyjnego.');
                return;
            }

            // Wysłanie żądania DELETE do serwera
            await axios.delete(`http://localhost:8080/api/v1/posters/match/${posterId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            // Usuwanie plakatu z lokalnego stanu
            setMatchPosters((prev) => prev.filter((poster) => poster.id !== posterId));
            navigate("/games");
        } catch (err) {
            setError('Nie udało się usunąć plakatu. Spróbuj ponownie później.');
        }
    };

    return (
        <div className="main-page">
            {error && <p className="error-message">{error}</p>}

            {/* Wyszukiwarka */}
            <div className="search-bar">
                <input
                    type="text"
                    placeholder="Szukaj plakatów..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="form-control"
                />
            </div>

            {/* Sortowanie */}
            <div className="sort-bar">
                <button onClick={handleSortChange} className="btn btn-secondary">
                    Sortuj według terminu: {sortOrder === 'asc' ? 'Rosnąco' : 'Malejąco'}
                </button>
            </div>

            {/* Lista plakatów */}
            <div className="posters-container">
                {sortedPosters.map((poster) => (
                    <div
                        key={poster.id}
                        className="poster-card"
                        onClick={() => navigate(`/matchpage/${poster.id}`)}
                        style={{ cursor: 'pointer' }}
                    >
                        <h3>{poster.title}</h3>
                        <p>{poster.description}</p>
                        <p>Termin zgłoszeń: {new Date(poster.dueDate).toLocaleDateString()}</p>
                        <p>Data utworzenia: {new Date(poster.createdAt).toLocaleDateString()}</p>
                        {/* Przycisk usuwania */}
                        <button
                            onClick={() => handleDeletePoster(poster.id)}
                            className="btn btn-danger"
                        >
                            Usuń
                        </button>
                    </div>
                ))}
                {sortedPosters.length === 0 && <p>Brak plakatów spełniających kryteria wyszukiwania.</p>}
            </div>
        </div>
    );
};

export default MainPage;
