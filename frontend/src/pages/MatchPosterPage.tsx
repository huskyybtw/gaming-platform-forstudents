import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../styles/MatchPosterPage.css';
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

const MatchPosterPage: React.FC = () => {
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
                setError('Failed to load posters. Please try again later. error: ' + err);

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

    /* Funkcja usuwania plakatu
    const handleDeletePoster = async (posterId: number) => {
        try {
            const token = Cookies.get('token');
            if (!token) {
                setError('Missing authorization token.');
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
            setError('Failed to delete the poster. Please try again later. error: ' + err);
        }
    };
    */

    return (
        <div className="main-page">
            {error && <p className="error-message">{error}</p>}

            {/* Search bar */}
            <div className="search-bar">
                <input
                    type="text"
                    placeholder="Search posters..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="form-control"
                />
            </div>

            {/* Sorting */}
            <div className="sort-bar">
                <button onClick={handleSortChange} className="btn btn-secondary">
                    Sort by deadline: {sortOrder === 'asc' ? 'Ascending' : 'Descending'}
                </button>
            </div>

            {/* Posters list */}
            <div className="posters-container">
                {sortedPosters.map((poster) => (
                    <div
                        key={poster.id}
                        className="poster-card"
                        onClick={() => navigate(`/games/${poster.id}`)}
                        style={{ cursor: 'pointer' }}
                    >
                        <h3>{poster.title}</h3>
                        <p>{poster.description}</p>
                        <p>Submission deadline: {new Date(poster.dueDate).toLocaleDateString()}</p>
                        <p>Creation date: {new Date(poster.createdAt).toLocaleDateString()}</p>
                    </div>
                ))}
                {sortedPosters.length === 0 && <p>No posters matching the search criteria.</p>}
            </div>
        </div>
    );

};

export default MatchPosterPage;
