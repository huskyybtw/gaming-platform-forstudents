import { useEffect, useState } from "react";
import axios from "axios";
import '../styles/MatchHistoryDisplay.css';

interface Props {
    userId: number;
    puuid?: string;
    limit?: number;
}

interface MatchDetail {
    matchId: string;
    endOfGameResult: string;
    gameDuration: number;
    gameVersion: string;
    winner: number;
    participant: Array<{
        puuid: string;
        playerId: string;
        championId: number;
        championName: string;
        championLevel: number;
        kills: number;
        deaths: number;
        assists: number;
        totalMinionsKilled: number;
        role: string;
        win: boolean;
        item0: number;
        item1: number;
        item2: number;
        item3: number;
        item4: number;
        item5: number;
        item6: number;
    }>;
}

function MatchHistoryDisplay(props: Props) {
    const [matchesData, setMatchesData] = useState<string[]>([]);
    const [matchesDetails, setMatchesDetails] = useState<MatchDetail[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [itemIcons, setItemIcons] = useState<{ [key: number]: string }>({});

    // Fetch match IDs using PUUID
    useEffect(() => {
        const fetchData = async () => {
            try {
                if (props.puuid) {
                    const response = await axios.get(`${import.meta.env.VITE_BACKEND_URI}/testing/riotService/userMatches?puuid=${props.puuid}`);
                    const parsedData = JSON.parse(response.data[0]);
                    setMatchesData(parsedData);
                }
            } catch (err) {
                console.error("Error fetching match IDs:", err);
            }
        };

        fetchData();
    }, [props.puuid]);

    // Fetch match details based on match IDs
    useEffect(() => {
        const fetchMatchDetails = async () => {
            setLoading(true);

            try {
                const matchPromises = matchesData.map((matchId) =>
                    axios.get(`http://localhost:8080/testing/riotService/match?matchid=${matchId}`)
                );

                const matchResponses = await Promise.all(matchPromises);
                setMatchesDetails(matchResponses.map((response) => response.data));
            } catch (error) {
                console.error("Error fetching match details:", error);
            } finally {
                setLoading(false);
            }
        };

        if (matchesData.length > 0) {
            fetchMatchDetails();
        }
    }, [matchesData]);

    // Helper function to get champion icon URL using champion ID
    const getChampionIcon = (championId: number) => {
        return `https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/${championId}.png`;
    };

    // Helper function to get role icon URL based on participant role
    const getRoleIcon = (role: string) => {
        return `https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-clash/global/default/assets/images/position-selector/positions/icon-position-${role.toLowerCase()}.png`;
    };

    // Helper function to fetch item icon URL using item ID
    const getItemIcon = async (itemId: number) => {
        const proxyUrl = "https://corsproxy.io/?url=";
        const apiUrl = `https://cdn.merakianalytics.com/riot/lol/resources/latest/en-US/items/${itemId}.json`;

        try {
            const response = await axios.get(proxyUrl + encodeURIComponent(apiUrl));
            return response.data.icon;
        } catch (error) {
            console.error("Error fetching item icon:", error);
        }
    };

    // Fetch item icons and update the state
    useEffect(() => {
        const fetchItemIcons = async () => {
            const iconMap: { [key: number]: string } = {};

            const items = [
                ...new Set(matchesDetails.flatMap((match) => match.participant.flatMap((player) => [
                    player.item0,
                    player.item1,
                    player.item2,
                    player.item3,
                    player.item4,
                    player.item5,
                    player.item6,
                ])))
            ];

            for (const itemId of items) {
                if (itemId !== 0 && !iconMap[itemId]) {
                    const iconUrl = await getItemIcon(itemId);
                    iconMap[itemId] = iconUrl;
                }
            }

            setItemIcons(iconMap);
        };

        if (matchesDetails.length > 0) {
            fetchItemIcons();
        }
    }, [matchesDetails]);

    // Helper function to convert game time from seconds to minutes
    const formatGameDuration = (duration: number) => {
        const minutes = Math.floor(duration / 60);
        const seconds = duration % 60;
        return `${minutes}m ${seconds}s`;
    };

    return (
        <div>
            <h2>Match History</h2>
            {loading ? (
                <p>Loading match details...</p>
            ) : (
                <ul className="list-group">
                    {matchesDetails.length > 0 ? (
                        matchesDetails.map((match) => {
                            // Find the player's participant based on their PUUID
                            const player = match.participant.find(
                                (participant) => participant.puuid === props.puuid
                            );

                            if (!player) return null; // If player is not found in this match, skip

                            // Get the champion icon URL for the player
                            const championIconUrl = getChampionIcon(player.championId);

                            // Get the role icon URL for the player using the provided link
                            const roleIconUrl = getRoleIcon(player.role);

                            // Determine win/loss class
                            const matchClass = player.win ? 'win' : 'loss';

                            // Get the item icons from the state
                            const items = [
                                player.item0,
                                player.item1,
                                player.item2,
                                player.item3,
                                player.item4,
                                player.item5,
                                player.item6
                            ];

                            return (
                                <li
                                    key={match.matchId}
                                    className={`list-group-item match-history-item ${matchClass}`}
                                    style={{
                                        backgroundColor: player.win ? '#d4edda' : '#f8d7da',
                                        border: '1px solid #ccc',
                                        padding: '10px',
                                        marginBottom: '10px',
                                        maxWidth: '300px', // Set maximum width to make the card smaller
                                    }}
                                >
                                    <div className="match-details">
                                        <h4 className="match-id" style={{ fontSize: '14px' }}>Match ID: {match.matchId}</h4>
                                        <div className="player-info" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }}>
                                            <div className="nickname-role" style={{ flex: '1', fontSize: '14px' }}>
                                                <h5 className="player-nickname" style={{ fontSize: '14px' }}>{player.playerId}</h5>
                                                <p className="role" style={{ fontSize: '12px' }}>{player.role}</p>
                                                {roleIconUrl && (
                                                    <img
                                                        src={roleIconUrl}
                                                        alt={player.role}
                                                        className="role-icon"
                                                        style={{ width: '32px', height: '32px' }} // Reduced size
                                                    />
                                                )}
                                            </div>
                                            <div className="champion-info" style={{ textAlign: 'right' }}>
                                                <div className="kda" style={{ fontSize: '16px' }}>
                                                    <span className="kills" style={{ fontWeight: 'bold' }}>{player.kills}</span>/
                                                    <span className="deaths" style={{ fontWeight: 'bold' }}>{player.deaths}</span>/
                                                    <span className="assists" style={{ fontWeight: 'bold' }}>{player.assists}</span>
                                                </div>
                                                <img
                                                    src={championIconUrl}
                                                    alt={player.championName}
                                                    className="champion-icon-img"
                                                    style={{ width: '32px', height: '32px' }} // Reduced size
                                                />
                                            </div>
                                        </div>

                                        <div className="level-items" style={{ fontSize: '12px', marginTop: '10px' }}>
                                            <p>Champion Level: {player.championLevel}</p>
                                            <div className="items" style={{ display: 'flex', gap: '5px', flexWrap: 'wrap' }}>
                                                <h6>Items:</h6>
                                                <div className="item-icons">
                                                    {items.map((itemId, index) => (
                                                        itemId !== 0 && itemIcons[itemId] && (
                                                            <img
                                                                key={index}
                                                                src={itemIcons[itemId]} // Display the item icon
                                                                alt={`Item ${index + 1}`}
                                                                className="item-icon"
                                                                style={{ width: '24px', height: '24px' }} // Reduced size
                                                            />
                                                        )
                                                    ))}
                                                </div>
                                            </div>
                                        </div>
                                        <p className="game-duration" style={{ fontSize: '12px' }}>Game Duration: {formatGameDuration(match.gameDuration)}</p>
                                    </div>
                                </li>
                            );
                        })
                    ) : (
                        <li>No match details available.</li>
                    )}
                </ul>
            )}
        </div>
    );
}

export default MatchHistoryDisplay;
