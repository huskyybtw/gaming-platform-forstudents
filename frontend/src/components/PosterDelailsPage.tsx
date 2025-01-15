import { useParams } from "react-router-dom";

function PosterDetailsPage() {
    const { id } = useParams<{ id: string }>();

    // Symulowane dane. W prawdziwej aplikacji dane pobierz z API
    const matchPoster = {
        id,
        title: `Match Poster ${id}`,
        description: `Details about Match Poster ${id}`,
        usersLeft: [
            { id: 1, name: "User 1" },
            { id: 2, name: "User 2" },
            { id: 3, name: "User 3" },
            { id: 4, name: "User 4" },
            { id: 5, name: "User 5" },
        ],
        usersRight: [
            { id: 6, name: "User 6" },
            { id: 7, name: "User 7" },
            { id: 8, name: "User 8" },
            { id: 9, name: "User 9" },
            { id: 10, name: "User 10" },
        ],
    };

    return (
        <div className="container mt-4">
            {/* Informacje o plakacie */}
            <div className="mb-4">
                <h1>{matchPoster.title}</h1>
                <p>{matchPoster.description}</p>
            </div>

            {/* Layout 2 tabelki */}
            <div className="row">
                {/* Lewa tabelka */}
                <div className="col-md-6">
                    <h2>Lewa drużyna</h2>
                    <table className="table table-bordered">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Imię</th>
                        </tr>
                        </thead>
                        <tbody>
                        {matchPoster.usersLeft.map((user) => (
                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td>{user.name}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>

                {/* Prawa tabelka */}
                <div className="col-md-6">
                    <h2>Prawa drużyna</h2>
                    <table className="table table-bordered">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Imię</th>
                        </tr>
                        </thead>
                        <tbody>
                        {matchPoster.usersRight.map((user) => (
                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td>{user.name}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}

export default PosterDetailsPage;
