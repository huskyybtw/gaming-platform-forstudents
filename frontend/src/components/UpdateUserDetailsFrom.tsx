import {ChangeEvent, FormEvent, useEffect, useState} from 'react';
import * as React from "react";
import axios from "axios";

interface UserData {
    email: string;
    password: string;
    confirmPassword: string;
}

const headers ={
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${"test"}`
}

function UpdateUserDetailsForm() {

    const [userData, setUserData] = useState({} as UserData);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(`${import.meta.env.VITE_BACKEND_URI}/api/v1/users/1`, {headers});
                setUserData(response.data);
            } catch (err) {
                console.error("Error fetching data:", err);
            }
        };

        fetchData();
    }, []);

    const [isEditing, setIsEditing] = useState(false);

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
            alert("Passwords do not match!");
            return;
        }
        // Add form submission logic here
    };

    // Close the modal
    const closeModal = () => {
        setIsEditing(false);
    };

    return (
        <div>
            <h2>Profile Details</h2>

            {/* Display user details */}
            <div>
                <ul className={"list-group"}>
                    <li className={"list-group-item"}><strong>Email:</strong> {userData.email}</li>
                </ul>
                <br></br>
                <button className="btn btn-warning" onClick={() => setIsEditing(true)}>
                    Change Password
                </button>
            </div>

            {/* Modal for editing */}
            {isEditing && (
                <div style={modalBackdropStyle}>
                    <div style={modalContainerStyle}>
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
                                <button
                                    type="button"
                                    className="btn btn-secondary"
                                    onClick={closeModal}
                                >
                                    Cancel
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}

// Inline styles for the modal
const modalBackdropStyle: React.CSSProperties = {
    position: 'fixed',
    top: 0,
    left: 0,
    width: '100%',
    height: '100%',
    backgroundColor: 'rgba(0, 0, 0, 0.5)', // Semi-transparent background
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: 1000,
};

const modalContainerStyle: React.CSSProperties = {
    backgroundColor: '#fff',
    padding: '20px',
    borderRadius: '8px',
    width: '400px',
    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
};

export default UpdateUserDetailsForm;
