import { useEffect, useState } from "react";
import axios from "axios";

interface RankingData {
    leagueId: string;
    queueType: string;
    tier: string;
    rank: string;
    summonerId: string;
    leaguePoints: number;
    wins: number;
    losses: number;
    hotStreak: boolean;
    veteran: boolean;
    freshBlood: boolean;
    inactive: boolean;
    miniSeries: boolean;
    target: number;
    progress: number;
}

function PlayerRankDisplay() {
    const [profileData, setProfileData] = useState<RankingData[]>([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(`${import.meta.env.VITE_BACKEND_URI}/api/v1/players/rank/1`);
                setProfileData(response.data);
            } catch (err) {
                console.error("Error fetching data:", err);
            }
        };

        fetchData();
    }, []);
    console.log(profileData);
    const getRankIcon = (tier: string): string => {
        if(tier === "unranked") {
            return `https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/unranked-emblem.png`;
        }
        return `https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-${tier.toLowerCase()}.png`;
    };

    return (
        <div>
            <h1>Player Rank Display</h1>
            <div>
                {profileData.map((rank) => (
                    <div
                        key={rank.leagueId}
                        style={{
                            marginBottom: "20px",
                            padding: "10px",
                            border: "1px solid #ccc",
                            display: "flex",
                            alignItems: "center",
                            gap: "15px",
                        }}
                    >
                        <img
                            src={getRankIcon(rank.tier)}
                            alt={`${rank.tier} rank emblem`}
                            style={{
                            width: "200px",
                            height: "200px",
                            objectFit: "contain",
                            padding: "10px",
                            backgroundColor: "#f9f9f9",
                            borderRadius: "10px",
                            boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
                        }}
                        />

                        <div>
                            <h2>{rank.queueType.replace("_", " ")}</h2>
                            <p>
                                <strong>Tier:</strong> {rank.tier} {rank.rank}
                            </p>
                            <p>
                                <strong>League Points:</strong> {rank.leaguePoints} LP
                            </p>
                            <p>
                                <strong>Wins:</strong> {rank.wins} | <strong>Losses:</strong> {rank.losses}
                            </p>
                            <p>
                                <strong>Win Rate:</strong>{" "}
                                {((rank.wins + rank.losses) === 0 ? "0.00" : ((rank.wins / (rank.wins + rank.losses)) * 100).toFixed(2))}%
                            </p>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default PlayerRankDisplay;
