import { ChangeEvent, FormEvent, useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import "../styles/UpdateFromStyles.css"; // Import the CSS file

interface UserData {
    email: string;
    password: string;
    confirmPassword: string;
}

function UpdateUserDetailsForm() {
    const [userData, setUserData] = useState({} as UserData);
    const [isEditing, setIsEditing] = useState(false);

    const [showToast, setShowToast] = useState(false);
    const [toastMessage, setToastMessage] = useState("");
    const [toastType, setToastType] = useState("success");

    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const headers = {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${Cookies.get("token")}`,
                };

                const response = await axios.get(
                    `${import.meta.env.VITE_BACKEND_URI}/api/v1/users/${Cookies.get("userId")}`,
                    { headers }
                );
                setUserData(response.data);
            } catch (err: any) {
                if (err.response && err.response.status === 403) {
                    navigate("/forbidden");
                } else {
                    console.error("Error fetching data:", err);
                }
            }
        };

        fetchData();
    }, [navigate]);

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setUserData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        if (userData.password !== userData.confirmPassword) {
            showToastNotification("Passwords do not match!", "danger");
            return;
        }
        showToastNotification("Password updated successfully!", "success");
    };

    const showToastNotification = (message: string, type: string) => {
        setToastMessage(message);
        setToastType(type);
        setShowToast(true);

        setTimeout(() => {
            setShowToast(false);
        }, 3000);
    };

    const closeModal = () => {
        setIsEditing(false);
    };

    return (
        <div>
            <h2>Profile Details</h2>
            <div>
                <ul className="list-group">
                    <li className="list-group-item">
                        <strong>Email:</strong> {userData.email}
                    </li>
                </ul>
                <br />
                <button className="btn btn-warning" onClick={() => setIsEditing(true)}>
                    Edit Email/password
                </button>
            </div>

            {isEditing && (
                <div className="modal-backdrop">
                    <div className="modal-container">
                        <button
                            type="button"
                            onClick={closeModal}
                            className="close-button"
                            aria-label="Close"
                        >
                            &times;
                        </button>

                        <h2>Update Email</h2>
                        <form onSubmit={handleSubmit}>
                            <div className="mb-3">
                                <input
                                    type="text"
                                    className="form-control"
                                    id="email"
                                    name="email"
                                    onChange={handleChange}
                                    placeholder="example@gmail.com"
                                />
                            </div>
                            <div>
                                <button type="submit" className="btn btn-primary">
                                    Update
                                </button>
                            </div>
                        </form>

                        <br />

                        <h2>Update Password</h2>
                        <form onSubmit={handleSubmit}>
                            <div className="mb-3">
                                <input
                                    type="password"
                                    className="form-control"
                                    id="password"
                                    name="password"
                                    onChange={handleChange}
                                    placeholder="Password"
                                />
                            </div>
                            <div className="mb-3">
                                <input
                                    type="password"
                                    className="form-control"
                                    id="confirmPassword"
                                    name="confirmPassword"
                                    onChange={handleChange}
                                    placeholder="Confirm Password"
                                />
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

export default UpdateUserDetailsForm;
