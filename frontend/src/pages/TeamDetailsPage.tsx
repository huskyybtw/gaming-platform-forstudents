import { useEffect, useState } from "react";
import axios from "axios";
import { useParams, Link } from "react-router-dom";
import NavBar from "../components/NavBar";
import SideBar from "../components/SideBar";
import Footer from "../components/Footer";
import TeamSearchBar from "../components/TeamSearchBar.tsx";
import Cookies from "js-cookie";

interface Player {
    userId: number; // Using userId
    nickname: string;
    tagLine: string;
    rating: number;
    summonerLevel: number;
    description: string;
    profileIconId: number;
}

interface Team {
    id: number;
    teamName: string;
    description: string;
    teamCaptain: number;
    rating: number;
}

interface TeamResponse {
    team: Team;
    users: number[];
}

function TeamDetailsPage() {
    const { teamid } = useParams<{ teamid: string }>(); // Get teamid from URL
    const [team, setTeam] = useState<Team | null>(null);
    const [players, setPlayers] = useState<Player[]>([]);
    const [captain, setCaptain] = useState<Player | null>(null); // State to hold captain data
    const [error, setError] = useState<string | null>(null);

    const [isEditing, setIsEditing] = useState(false); // To toggle modal for editing team
    const [teamName, setTeamName] = useState(""); // State for team name
    const [teamDescription, setTeamDescription] = useState(""); // State for team description
    const [toastMessage, setToastMessage] = useState("");
    const [toastType, setToastType] = useState(""); // Success or error type for the toast

    const [newPlayerId, setNewPlayerId] = useState<number | string>("");

    useEffect(() => {
        if (!teamid) return;

        const fetchTeamData = async () => {
            try {
                // Fetch team details
                const teamResponse = await axios.get<TeamResponse>(
                    `${import.meta.env.VITE_BACKEND_URI}/api/v1/teams/${teamid}`
                );
                setTeam(teamResponse.data.team);

                // Fetch player details based on team users
                const userIds = teamResponse.data.users;
                const playerRequests = userIds.map((userId) =>
                    axios.get<Player>(`${import.meta.env.VITE_BACKEND_URI}/api/v1/players/${userId}`)
                );

                const playerResponses = await Promise.all(playerRequests);
                setPlayers(playerResponses.map((response) => response.data));

                // Fetch captain data using the captain id from the team data
                const captainResponse = await axios.get<Player>(
                    `${import.meta.env.VITE_BACKEND_URI}/api/v1/players/${teamResponse.data.team.teamCaptain}`
                );
                setCaptain(captainResponse.data);
            } catch (err) {
                setError("Error fetching team or player data.");
            }
        };

        fetchTeamData();
    }, [teamid]);

    const handleEditClick = () => {
        if (team) {
            setTeamName(team.teamName);
            setTeamDescription(team.description);
        }
        setIsEditing(true);
    };

    const handleCloseModal = () => {
        setIsEditing(false);
    };

    const handleSaveChanges = async () => {
        try {
            const requestBody = {
                teamName: teamName,
                description: teamDescription,
                teamCaptain: team?.teamCaptain,
                rating: team?.rating
            };

            const headers = {
                "Content-Type": "application/json",
                Authorization: `Bearer ${Cookies.get("token")}`, // Use token from cookies
            };

            // Send PUT request to update the team
            await axios.put(
                `${import.meta.env.VITE_BACKEND_URI}/api/v1/teams/${team?.id}`,
                requestBody,
                { headers }
            );

            // Update the team data with the new information
            setTeam((prevTeam) => ({
                ...prevTeam!,
                teamName: teamName,
                description: teamDescription
            }));

            showToastNotification("Team updated successfully!", "success");
            setIsEditing(false); // Close the modal after successful update
        } catch (error: any) {
            showToastNotification("Error updating team.", "danger");
        }
    };

    const handleRemovePlayer = async (userId: number) => {
        try {
            // Get the teamId and userId to make the API request
            const teamId = team?.id;

            if (!teamId) {
                console.error("Team ID is not available");
                return;
            }

            // Call the API to remove the player from the team
            const response = await axios.delete(
                `${import.meta.env.VITE_BACKEND_URI}/api/v1/teams/manage/${userId}/${teamId}`,
                {
                    headers: {
                        Authorization: `Bearer ${Cookies.get("token")}`, // Use token from cookies
                    },
                }
            );

            // After successful removal, update the players state
            if (response.status === 200) {
                // Filter out the removed player from the state
                setPlayers((prevPlayers) => prevPlayers.filter((player) => player.userId !== userId));

                showToastNotification("Player removed successfully!", "success");
            }
        } catch (error) {
            console.error("Error removing player:", error);
            showToastNotification("Error removing player.", "danger");
        }
    };

    // Handle adding a player to the team
    const handleAddPlayer = async () => {
        try {
            const teamId = team?.id;

            if (!teamId || !newPlayerId) {
                console.error("Invalid teamId or newPlayerId");
                return;
            }

            // Call the API to add the player to the team
            const response = await axios.post(
                `${import.meta.env.VITE_BACKEND_URI}/api/v1/teams/manage/${newPlayerId}/${teamId}`,
                {},
                {
                    headers: {
                        Authorization: `Bearer ${Cookies.get("token")}`, // Use token from cookies
                    },
                }
            );

            if (response.status === 200) {
                const addedPlayer = await axios.get<Player>(
                    `${import.meta.env.VITE_BACKEND_URI}/api/v1/players/${newPlayerId}`
                );
                setPlayers((prevPlayers) => [...prevPlayers, addedPlayer.data]);

                showToastNotification("Player added successfully!", "success");
                setNewPlayerId(""); // Clear the input after successful addition
            }
        } catch (error) {
            console.error("Error adding player:", error);
            showToastNotification("Error adding player.", "danger");
        }
    };

    const showToastNotification = (message: string, type: string) => {
        setToastMessage(message);
        setToastType(type);

        setTimeout(() => {
            setToastMessage(""); // Hide the toast after 3 seconds
        }, 3000);
    };

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div className="d-flex flex-column vh-100">
            <NavBar />
            <div className="d-flex flex-grow-1">
                <main className="d-flex flex-column flex-grow-1" style={{ gridTemplateColumns: "1fr 1fr" }}>
                    <div style={{ flex: 0.1 }}>
                        <TeamSearchBar />
                    </div>
                    <div style={{ flex: 0.2 }}>
                        <h1>Team Details</h1>
                        {/* Display Edit Button for Captain if Logged-in User is the Captain */}
                        {captain && captain.userId === parseInt(Cookies.get("userId")!) && (
                            <button className="btn btn-warning mt-2" onClick={handleEditClick}>
                                Edit Team
                            </button>
                        )}

                        {team && (
                            <ul>
                                <li>Team Name: {team.teamName}</li>
                                <li>Description: {team.description}</li>
                                <li>
                                    <h5>Captain: </h5>
                                    {captain && (
                                        <Link to={`/profile/${captain.userId}`} className="d-flex align-items-center text-decoration-none">
                                            <img
                                                src={`https://ddragon.leagueoflegends.com/cdn/14.24.1/img/profileicon/${captain.profileIconId}.png`}
                                                alt={`Profile Icon ${captain.profileIconId}`}
                                                className="profile-icon me-3"
                                                style={{ width: "30px", height: "30px", borderRadius: "50%" }}
                                            />
                                            <strong>{captain.nickname}#{captain.tagLine}</strong>
                                        </Link>
                                    )}
                                </li>
                                <li>Rating: {team.rating}</li>
                            </ul>
                        )}
                    </div>
                    <div style={{ flex: 0.7 }}>
                        <h2>Team Members</h2>
                        <ul className="list-group">
                            {players.map((player) => (
                                <li key={player.userId} className="list-group-item d-flex align-items-center mb-3" style={{ position: "relative" }}>
                                    <Link to={`/profile/${player.userId}`} className="d-flex align-items-center text-decoration-none">
                                        <img
                                            src={`https://ddragon.leagueoflegends.com/cdn/14.24.1/img/profileicon/${player.profileIconId}.png`}
                                            alt={`Profile Icon ${player.profileIconId}`}
                                            className="profile-icon me-3"
                                            style={{ width: "30px", height: "30px", borderRadius: "50%" }}
                                        />
                                        <strong>{player.nickname}#{player.tagLine}</strong>
                                    </Link>
                                    <p className="ms-2 mb-0">Rating: {player.rating}</p>
                                    <p className="ms-3 mb-0" style={{ whiteSpace: "pre" }}>{player.description}</p>

                                    {/* Red "X" button for the captain */}
                                    {captain && captain.userId === parseInt(Cookies.get("userId")!) && (
                                        <button
                                            className="btn btn-danger ms-auto"
                                            style={{
                                                position: "absolute",
                                                right: "10px",
                                                top: "50%",
                                                transform: "translateY(-50%)"
                                            }}
                                            onClick={(e) => {
                                                e.stopPropagation(); // Zatrzymaj propagację zdarzenia
                                                handleRemovePlayer(player.userId);
                                            }}
                                        >
                                            X
                                        </button>

                                    )}
                                </li>
                            ))}
                        </ul>

                        {/* Add Player Section */}
                        {captain && captain.userId === parseInt(Cookies.get("userId")!) && (
                            <div className="mt-3">
                                <h4>Add Player</h4>
                                <div className="d-flex">
                                    <input
                                        type="number"
                                        className="form-control me-2"
                                        placeholder="Enter User ID"
                                        value={newPlayerId}
                                        onChange={(e) => setNewPlayerId(e.target.value)}
                                    />
                                    <button
                                        className="btn btn-primary"
                                        onClick={handleAddPlayer}
                                        disabled={!newPlayerId}
                                    >
                                        Add Player
                                    </button>
                                </div>
                            </div>
                        )}
                    </div>
                </main>

                <aside className="bg-light" style={{ width: "200px", minWidth: "200px" }}>
                    <SideBar />
                </aside>
            </div>
            <Footer />

            {/* Modal for Editing Team */}
            {isEditing && (
                <div className="modal-backdrop">
                    <div className="modal-container">
                        <h2>Edit Team</h2>
                        <form onSubmit={(e) => e.preventDefault()}>
                            <button
                                type="button"
                                onClick={handleCloseModal}
                                className="close-button"
                                aria-label="Close"
                            >
                                &times;
                            </button>
                            <div className="mb-3">
                                <label htmlFor="teamName" className="form-label">
                                    Team Name
                                </label>
                                <input
                                    type="text"
                                    className="form-control"
                                    id="teamName"
                                    value={teamName}
                                    onChange={(e) => setTeamName(e.target.value)}
                                    placeholder="Enter team name"
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="teamDescription" className="form-label">
                                    Team Description
                                </label>
                                <textarea
                                    className="form-control"
                                    id="teamDescription"
                                    value={teamDescription}
                                    onChange={(e) => setTeamDescription(e.target.value)}
                                    placeholder="Enter team description"
                                    rows={3}
                                    required
                                />
                            </div>
                            <button
                                type="submit"
                                className="btn btn-success"
                                onClick={handleSaveChanges}
                            >
                                Save Changes
                            </button>
                        </form>
                    </div>
                </div>
            )}

            {/* Toast notification */}
            {toastMessage && (
                <div className={`toast ${toastType === "success" ? "toast-success" : "toast-danger"}`}>
                    {toastMessage}
                </div>
            )}
        </div>
    );
}

export default TeamDetailsPage;
