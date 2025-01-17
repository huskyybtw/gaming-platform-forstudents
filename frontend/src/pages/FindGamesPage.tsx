import { useState, useEffect, ChangeEvent, FormEvent } from "react";
import NavBar from "../components/NavBar";
import MatchPosters from "./MatchPosterPage.tsx";
import Footer from "../components/Footer";
import Cookies from "js-cookie";
import {useNavigate} from "react-router-dom";

function FindGamesPage() {
    const [isFormVisible, setIsFormVisible] = useState(false);
    const [formData, setFormData] = useState({
        ranked: false,
        description: "",
        dueDate: "",
    });

    // Dodaj stan do wymuszania odświeżenia
    const [refreshKey, setRefreshKey] = useState(0);
    const navigate = useNavigate();

    useEffect(() => {
        const userId = Cookies.get("userId");
        if (userId) {
            console.log("Logged in user with ID:", userId);
        }
    }, []);

    const handleInputChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value, type } = e.target;
        const checked = type === "checkbox" ? (e.target as HTMLInputElement).checked : undefined;
        setFormData({
            ...formData,
            [name]: type === "checkbox" ? checked : value,
        });
    };

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const token = Cookies.get("token");
        const ownerId = Cookies.get("userId");

        if (!token) {
            alert("Authorization token not found!");
            return;
        }

        if (!ownerId) {
            alert("User ID not found!");
            return;
        }


        const dataToSend = {
            ...formData,
            ownerId,
        };

        try {
            const response = await fetch("http://localhost:8080/api/v1/posters/match/", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(dataToSend),
            });

            const textResponse = await response.text();
            console.log("Response:", textResponse);

            if (response.ok) {
                alert("The announcement has been added!");
                setIsFormVisible(false);

                // Wymuś odświeżenie listy
                setRefreshKey((prevKey) => prevKey + 1);
                // Przekierowanie na stronę /games po pomyślnym dodaniu plakatu
                navigate("/games"); // Przekierowanie
            } else {
                try {
                    const error = JSON.parse(textResponse);
                    alert(`Error: ${error.message}`);
                } catch {
                    alert(`Error: Unexpected response received: ${textResponse}`);
                }
            }

        } catch (error) {
            console.error("Wystąpił problem z połączeniem:", error);
        }
    };

    return (
        <div className="d-flex flex-column vh-100">
            <NavBar />
            <main className="flex-grow-1 p-4">
                <h1>Find Games</h1>
                <button
                    className="btn btn-primary mb-3"
                    onClick={() => setIsFormVisible(true)}
                >
                    Add Announcement
                </button>

                {isFormVisible && (
                    <form onSubmit={handleSubmit} className="mb-4">
                        <div className="form-group">
                            <label>
                                Ranked:
                                <input
                                    type="checkbox"
                                    className="form-check-input"
                                    name="ranked"
                                    checked={formData.ranked}
                                    onChange={handleInputChange}
                                />
                            </label>
                        </div>
                        <div className="form-group">
                            <label>
                                Description:
                                <textarea
                                    className="form-control"
                                    name="description"
                                    value={formData.description}
                                    onChange={handleInputChange}
                                    required
                                />
                            </label>
                        </div>
                        <div className="form-group">
                            <label>
                                Due Date:
                                <input
                                    type="datetime-local"
                                    className="form-control"
                                    name="dueDate"
                                    value={formData.dueDate}
                                    onChange={handleInputChange}
                                    required
                                />
                            </label>
                        </div>
                        <button type="submit" className="btn btn-success">
                            Add
                        </button>
                        <button
                            type="button"
                            className="btn btn-secondary ml-2"
                            onClick={() => setIsFormVisible(false)}
                        >
                            Cancel
                        </button>
                    </form>
                )}

                {/* Pass refresh key */}
                <MatchPosters key={refreshKey} />
            </main>
            <Footer />
        </div>
    );

}

export default FindGamesPage;
