import { ChangeEvent, useEffect, useState } from "react";
import axios from "axios";
import * as React from "react";

interface ProfileData {
    nickname: string;
    tagLine: string;
    rating: number;
    summonerLevel: number;
    profileIconId: number;
    description: string;
}

interface Props {
    userId: number
    isLoggedUser: boolean
}


function UpdateRiotDetailsFrom(props: Props) {
    const [profileData, setProfileData] = useState({} as ProfileData);
    const [isEditing, setIsEditing] = useState(false);
    const [showToast, setShowToast] = useState(false);
    const [toastMessage, setToastMessage] = useState("");
    const [toastType, setToastType] = useState("success"); // 'success' or 'danger'

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(
                    `${import.meta.env.VITE_BACKEND_URI}/api/v1/players/${props.userId}`
                );
                setProfileData(response.data);
            } catch (err) {
                console.error("Error fetching data:", err);
            }
        };

        fetchData();
    }, []);

    const refreshData = async () => {
        try {
            const response = await axios.get(
                `${import.meta.env.VITE_BACKEND_URI}/api/v1/players/details/${props.userId}`
            );
            setProfileData(response.data);
            showToastNotification("Data refreshed successfully!", "success");
        } catch (err) {
            showToastNotification("Error refreshing data.", "danger");
        }
    };

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setProfileData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleSubmit = (e: ChangeEvent<HTMLFormElement>) => {
        e.preventDefault();
        console.log("Updated user data:", profileData);
        setIsEditing(false);
        showToastNotification("Profile updated successfully!", "success");
    };

    const closeModal = () => {
        setIsEditing(false);
    };

    const showToastNotification = (message: string, type: string) => {
        setToastMessage(message);
        setToastType(type);
        setShowToast(true);

        setTimeout(() => {
            setShowToast(false);
        }, 3000);
    };

    return (
        <div>
            <h2>Riot Details</h2>
            <div>
                {profileData && (
                    <ul className="list-group">
                        <li className="list-group-item">
                            Nickname: {profileData.nickname}#{profileData.tagLine}
                        </li>
                        <li className="list-group-item">Rating: {profileData.rating}</li>
                        <li className="list-group-item">
                            Summoner Level: {profileData.summonerLevel}
                        </li>
                        <li className="list-group-item">
                            {profileData.profileIconId && (
                                <img
                                    src={`https://ddragon.leagueoflegends.com/cdn/14.24.1/img/profileicon/${profileData.profileIconId}.png`}
                                    alt={`Profile Icon ${profileData.profileIconId}`}
                                    style={{ width: "50px", height: "50px", borderRadius: "50%" }}
                                />
                            )}
                        </li>
                        <li className="list-group-item">
                            <a
                                href={`https://www.op.gg/summoners/eune/${profileData.nickname}-${profileData.tagLine}`}
                                target="_blank"
                                rel="noopener noreferrer"
                                style={{ display: "flex", alignItems: "center" }}
                            >
                                <img
                                    src="https://cdn.brandfetch.io/idrLeSINfM/w/400/h/400/theme/dark/icon.jpeg?c=1dxbfHSJFAPEGdCLU4o5B"
                                    alt="op.gg icon"
                                    style={{ width: "24px", height: "24px", marginRight: "10px" }}
                                />
                            </a>
                        </li>
                        <li className="list-group-item">Description: {profileData.description}</li>
                    </ul>
                )}
                <br />
                {props.isLoggedUser && (
                    <button className="btn btn-warning" onClick={() => setIsEditing(true)}>
                        Edit
                    </button>
                )}
                <button className="btn btn-danger" onClick={() => refreshData()}>
                    Refresh
                </button>
            </div>

            {isEditing && (
                <div style={modalBackdropStyle}>
                    <div style={{...modalContainerStyle, position: "relative"}}>
                        <h2>Update Profile</h2>
                        <form onSubmit={handleSubmit}>
                            <div className="mb-3">
                                <button
                                    type="button"
                                    onClick={closeModal}
                                    style={{
                                        position: "absolute",
                                        top: "10px",
                                        right: "10px",
                                        backgroundColor: "transparent",
                                        border: "none",
                                        fontSize: "24px",
                                        fontWeight: "bold",
                                        color: "#333",
                                        cursor: "pointer",
                                    }}
                                    aria-label="Close"
                                >
                                    &times;
                                </button>
                                <label htmlFor="username" className="form-label">
                                    Riot ID
                                </label>
                                <div className="input-group">
                                    <div className="row w-100">
                                        <div className="col-8">
                                            <input
                                                type="text"
                                                className="form-control"
                                                id="username"
                                                name="username"
                                                value={profileData.nickname}
                                                onChange={handleChange}
                                                placeholder="Faker"
                                                required
                                            />
                                        </div>
                                        <div className="col-4">
                                            <input
                                                type="text"
                                                className="form-control"
                                                id="changetagLine"
                                                name="changetaLine"
                                                value={profileData.tagLine}
                                                onChange={handleChange}
                                                placeholder="1234"
                                                required
                                            />
                                        </div>
                                    </div>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="text" className="form-label">
                                        Description
                                    </label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        id="changeDescription"
                                        name="description"
                                        value={profileData.description}
                                        onChange={handleChange}
                                        placeholder="Write a description"
                                        required
                                    />
                                </div>
                            </div>
                            <div>
                                <button type="submit" className="btn btn-primary">
                                    Update
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            {showToast && (
                <div
                    className={`toast align-items-center show ${toastType === "success" ? "bg-success" : "bg-danger"}`}
                    role="alert"
                    aria-live="assertive"
                    aria-atomic="true"
                    style={{position: "fixed", bottom: "20px", right: "20px", zIndex: 9999}}
                >
                    <div className="d-flex">
                    <div className="toast-body">
                            {toastMessage}
                        </div>
                        <button
                            type="button"
                            className="btn-close me-2 m-auto"
                            aria-label="Close"
                            onClick={() => setShowToast(false)}
                        ></button>
                    </div>
                </div>
            )}
        </div>
    );
}

const modalBackdropStyle: React.CSSProperties = {
    position: "fixed",
    top: 0,
    left: 0,
    width: "100%",
    height: "100%",
    backgroundColor: "rgba(0, 0, 0, 0.5)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    zIndex: 1000,
};

const modalContainerStyle: React.CSSProperties = {
    backgroundColor: "#fff",
    padding: "20px",
    borderRadius: "8px",
    width: "400px",
    boxShadow: "0 4px 8px rgba(0, 0, 0, 0.2)",
};

export default UpdateRiotDetailsFrom;
