import { Link } from "react-router-dom";

interface Team {
    id: number;
    teamName: string;
    rating: number;
}

interface TeamsListProps {
    teams: Team[];
}

function TeamsList({ teams }: TeamsListProps) {
    return (
        <ul className="list-group">
            {teams.map((team) => (
                <li
                    key={team.id}
                    className="list-group-item d-flex align-items-center mb-3"
                    style={{ position: "relative" }}
                >
                    <div className="d-flex flex-column">
                        <Link
                            to={`/teams/${team.id}`}
                            className="text-decoration-none"
                        >
                            <strong>Team Name: {team.teamName}</strong>
                        </Link>
                        <p className="mb-0">Rating: {team.rating}</p>
                    </div>
                </li>
            ))}
        </ul>
    );
}

export default TeamsList;
