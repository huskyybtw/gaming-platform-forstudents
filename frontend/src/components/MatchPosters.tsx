import React, { useEffect, useState } from 'react';
import axios from 'axios';

const MatchPosters: React.FC = () => {
    const [posters, setPosters] = useState([]);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        axios.get('http://localhost:8080/api/v1/posters/match/')
            .then((response) => {
                setPosters(response.data);
            })
            .catch(() => {
                setError('Nie udało się załadować danych.');
            });
    }, []);

    return (
        <div>
            {error && <p>{error}</p>}
            <div>
                {posters.map((poster: any) => (
                    <div key={poster.id} className="card">
                        <h2>{poster.title}</h2>
                        <p>{poster.description}</p>
                        <p>Deadline: {poster.dueDate}</p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default MatchPosters;
