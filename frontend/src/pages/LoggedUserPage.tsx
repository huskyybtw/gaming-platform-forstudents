import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './MainPage.css';

interface MatchPoster {
    id: number;
    title: string;
    description: string;
    dueDate: string;
    createdAt: string;
    updatedAt: string;
    type: 'MatchPoster';
}

interface TeamPoster {
    id: number;
    teamId: number;  // Zmieniliśmy teamName na teamId
    description: string;
    dueDate: string;
    createdAt: string;
    updatedAt: string;
    type: 'TeamPoster';
}

const MainPage: React.FC = () => {
    const [matchPosters, setMatchPosters] = useState<MatchPoster[]>([]);
    const [teamPosters, setTeamPosters] = useState<TeamPoster[]>([]);
    const [error, setError] = useState<string>('');

    useEffect(() => {
        const fetchPosters = async () => {
            try {
                const matchResponse = await axios.get<MatchPoster[]>('http://localhost:8080/api/v1/posters/match/');
                const teamResponse = await axios.get<TeamPoster[]>('http://localhost:8080/api/v1/posters/team/');

                // Mapujemy dane z API na odpowiedni format
                const formattedMatchPosters: MatchPoster[] = matchResponse.data.map((item: any) => ({
                    id: item.matchPoster.id,
                    title: `Match ${item.matchPoster.id}`,
                    description: item.matchPoster.description,
                    dueDate: item.matchPoster.dueDate,
                    createdAt: item.matchPoster.createdAt,
                    updatedAt: item.matchPoster.updatedAt,
                    type: 'MatchPoster',
                }));

                const formattedTeamPosters: TeamPoster[] = teamResponse.data.map((item: any) => ({
                    id: item.id,
                    teamId: item.teamId,
                    description: item.description,
                    dueDate: item.dueDate,
                    createdAt: item.createdAt,
                    updatedAt: item.updatedAt,
                    type: 'TeamPoster',
                }));

                setMatchPosters(formattedMatchPosters);
                setTeamPosters(formattedTeamPosters);
            } catch (err) {
                setError('Nie udało się załadować plakatów. Spróbuj ponownie później.');
            }
        };

        fetchPosters();
    }, []);

    return (
        <div className="main-page">
            <h1>Ogłoszenia</h1>
            {error && <p className="error-message">{error}</p>}

            <h2>Match Posters</h2>
            <div className="posters-container">
                {matchPosters.map((poster) => (
                    <div key={poster.id} className="poster-card">
                        <h3>{poster.title}</h3>
                        <p>{poster.description}</p>
                        <p>Data utworzenia: {new Date(poster.createdAt).toLocaleDateString()}</p>
                        <p>Termin zgłoszeń: {new Date(poster.dueDate).toLocaleDateString()}</p>
                        <p>Ostatnia aktualizacja: {new Date(poster.updatedAt).toLocaleDateString()}</p>
                    </div>
                ))}
            </div>

            <h2>Team Posters</h2>
            <div className="posters-container">
                {teamPosters.map((poster) => (
                    <div key={poster.id} className="poster-card">
                        <h3>Team {poster.teamId}</h3>
                        <p>{poster.description}</p>
                        <p>Data utworzenia: {new Date(poster.createdAt).toLocaleDateString()}</p>
                        <p>Termin zgłoszeń: {new Date(poster.dueDate).toLocaleDateString()}</p>
                        <p>Ostatnia aktualizacja: {new Date(poster.updatedAt).toLocaleDateString()}</p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default MainPage;
