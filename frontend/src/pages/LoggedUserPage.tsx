import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './MainPage.css';

interface MatchPoster {
    id: number;
    title: string;
    description: string;
    date: string;
    dueDate: string;
}

interface ApiResponse {
    matchPoster: {
        id: number;
        ranked: boolean;
        description: string;
        dueDate: string;
    };
    participants: {
        userId: number;
        matchId: number;
        teamId: number;
        riot_team_number: number;
    }[];
}

const MainPage: React.FC = () => {
    const [posters, setPosters] = useState<MatchPoster[]>([]);
    const [error, setError] = useState<string>('');

    useEffect(() => {
        const fetchPosters = async () => {
            try {
                const response = await axios.get<ApiResponse[]>('http://localhost:8080/api/v1/posters/match/');
                const formattedData: MatchPoster[] = response.data.map((item) => ({
                    id: item.matchPoster.id,
                    title: item.matchPoster.ranked ? 'Ranked Match' : 'Casual Match',
                    description: item.matchPoster.description,
                    date: item.matchPoster.dueDate,
                    dueDate: item.matchPoster.dueDate,
                }));
                setPosters(formattedData);
            } catch (err) {
                setError('Nie udało się załadować plakatów. Spróbuj ponownie później.');
            }
        };

        fetchPosters();
    }, []);

    return (
        <div className="main-page">
            <h1>Postery</h1>
            {error && <p className="error-message">{error}</p>}
            <div className="posters-container">
                {posters.map((poster) => (
                    <div key={poster.id} className="poster-card">
                        <h2>{poster.title}</h2>
                        <p>{poster.description}</p>
                        <p>Data utworzenia: {new Date(poster.date).toLocaleDateString()}</p>
                        <p>Termin zgłoszeń: {new Date(poster.dueDate).toLocaleDateString()}</p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default MainPage;
