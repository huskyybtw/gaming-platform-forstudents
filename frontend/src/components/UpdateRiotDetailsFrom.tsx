import {ChangeEvent, useEffect, useState} from "react";
import axios from "axios";
import "../styles/UpdateFromStyles.css"; // Import the CSS file

interface ProfileData {
    nickname: string;
    tagLine: string;
    rating: number;
    summonerLevel: number;
    profileIconId: number;
    description: string;
    puuid: string;
    accountId: string;
}

interface Props {
    userId: number;
    isLoggedUser: boolean;
    profileData: ProfileData;
}

function UpdateRiotDetailsForm(props: Props) {
    const [profileData, setProfileData] = useState({} as ProfileData);
    const [isEditing, setIsEditing] = useState(false);
    const [showToast, setShowToast] = useState(false);
    const [toastMessage, setToastMessage] = useState("");
    const [toastType, setToastType] = useState("success");

    useEffect(() => {
        setProfileData(props.profileData);
    }, [props.profileData]);

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

    const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
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
                                    className="profile-icon"
                                />
                            )}
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
                <div className="modal-backdrop">
                    <div className="modal-container">
                        <h2>Update Profile</h2>
                        <form onSubmit={handleSubmit}>
                            <button
                                type="button"
                                onClick={closeModal}
                                className="close-button"
                                aria-label="Close"
                            >
                                &times;
                            </button>
                            <div className="mb-3">
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
                                                name="changetagLine"
                                                value={profileData.tagLine}
                                                onChange={handleChange}
                                                placeholder="1234"
                                                required
                                            />
                                        </div>
                                    </div>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="description" className="form-label">
                                        Description
                                    </label>
                                    <textarea
                                        className="form-control"
                                        id="description"
                                        name="description"
                                        value={profileData.description}
                                        onChange={handleChange}
                                        placeholder="A short description about yourself"
                                        rows={3}
                                    ></textarea>
                                </div>
                                <div>
                                    <button type="submit" className="btn btn-primary">
                                        Update
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            {showToast && (
                <div
                    className={`toast-notification ${
                        toastType === "success" ? "bg-success" : "bg-danger"
                    }`}
                    role="alert"
                    aria-live="assertive"
                    aria-atomic="true"
                >
                    <div className="d-flex">
                        <div className="toast-body">{toastMessage}</div>
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

export default UpdateRiotDetailsForm;

