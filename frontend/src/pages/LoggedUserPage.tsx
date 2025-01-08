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
    teamId: number;
    description: string;
    dueDate: string;
    createdAt: string;
    updatedAt: string;
    type: 'TeamPoster';
}

interface UserPoster {
    id: number;
    userId: number;  // Dodajemy userId
    description: string;
    dueDate: string;
    createdAt: string;
    updatedAt: string;
    type: 'UserPoster';
}

const MainPage: React.FC = () => {
    const [matchPosters, setMatchPosters] = useState<MatchPoster[]>([]);
    const [teamPosters, setTeamPosters] = useState<TeamPoster[]>([]);
    const [userPosters, setUserPosters] = useState<UserPoster[]>([]); // Nowy stan dla UserPoster
    const [error, setError] = useState<string>('');

    useEffect(() => {
        const fetchPosters = async () => {
            try {
                const matchResponse = await axios.get<MatchPoster[]>('http://localhost:8080/api/v1/posters/match/');
                const teamResponse = await axios.get<TeamPoster[]>('http://localhost:8080/api/v1/posters/team/');
                const userResponse = await axios.get<UserPoster[]>('http://localhost:8080/api/v1/posters/user/'); // Pobieranie danych UserPoster

                // Mapowanie odpowiedzi na odpowiedni format
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

                const formattedUserPosters: UserPoster[] = userResponse.data.map((item: any) => ({
                    id: item.id,
                    userId: item.userId,
                    description: item.description,
                    dueDate: item.dueDate,
                    createdAt: item.createdAt,
                    updatedAt: item.updatedAt,
                    type: 'UserPoster',
                }));

                setMatchPosters(formattedMatchPosters);
                setTeamPosters(formattedTeamPosters);
                setUserPosters(formattedUserPosters);  // Ustawienie danych UserPoster
            } catch (err) {
                setError('Nie udało się załadować plakatów. Spróbuj ponownie później.');
            }
        };

        fetchPosters();
    }, []);

    return (
        <div className="main-page">
            <h1>Postery/demo</h1>
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

            <h2>User Posters</h2>
            <div className="posters-container">
                {userPosters.map((poster) => (
                    <div key={poster.id} className="poster-card">
                        <h3>User {poster.userId}</h3>
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
