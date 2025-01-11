import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../styles/MainPage.css';

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

    useEffect(() => {
        const fetchMatchPosters = async () => {
            try {
                const response = await axios.get<MatchPoster[]>('http://localhost:8080/api/v1/posters/match/');
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
                        onClick={() => console.log(`Kliknięto plakat ${poster.id}`)}
                        style={{ cursor: 'pointer' }}
                    >
                        <h3>{poster.title}</h3>
                        <p>{poster.description}</p>
                        <p>Termin zgłoszeń: {new Date(poster.dueDate).toLocaleDateString()}</p>
                        <p>Data utworzenia: {new Date(poster.createdAt).toLocaleDateString()}</p>
                    </div>
                ))}
                {sortedPosters.length === 0 && <p>Brak plakatów spełniających kryteria wyszukiwania.</p>}
            </div>
        </div>
    );
};

export default MainPage;
