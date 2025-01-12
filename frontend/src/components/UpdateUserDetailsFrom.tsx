import { ChangeEvent, FormEvent, useEffect, useState } from "react";
import * as React from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import Cookies from "js-cookie";

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
                console.log(response.data);
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
                <ul className={"list-group"}>
                    <li className={"list-group-item"}>
                        <strong>Email:</strong> {userData.email}
                    </li>
                </ul>
                <br />
                <button className="btn btn-warning" onClick={() => setIsEditing(true)}>
                    Edit Email/password
                </button>
            </div>

            {isEditing && (
                <div style={modalBackdropStyle}>
                    <div style={{ ...modalContainerStyle, position: "relative" }}>
                        {/* Close button */}
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

                        {/* Email Update Form */}
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

                        {/* Password Update Form */}
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
                    className={`toast align-items-center show ${
                        toastType === "success" ? "bg-success" : "bg-danger"
                    }`}
                    role="alert"
                    aria-live="assertive"
                    aria-atomic="true"
                    style={{
                        position: "fixed",
                        bottom: "20px",
                        right: "20px",
                        zIndex: 9999,
                        maxWidth: "300px",
                    }}
                >
                    <div className="d-flex">
                        <div className="toast-body">{toastMessage}</div>
                        <button
                            type="button"
                            className="btn-close me-2 m-auto"
                            data-bs-dismiss="toast"
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

export default UpdateUserDetailsForm;
