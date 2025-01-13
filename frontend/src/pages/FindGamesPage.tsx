import NavBar from "../components/NavBar";
import MatchPosters from "../components/MatchPosters";
import Footer from "../components/Footer";
import { useState, useEffect, ChangeEvent, FormEvent } from "react";
import Cookies from "js-cookie";

function FindGamesPage() {
    const [isFormVisible, setIsFormVisible] = useState(false);
    const [formData, setFormData] = useState({
        ranked: false,
        description: "",
        dueDate: "",
    });

    useEffect(() => {
        const userId = Cookies.get("userId");
        if (userId) {
            console.log("Zalogowany użytkownik o ID:", userId);
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

        if (!token) {
            alert("Nie znaleziono tokenu autoryzacyjnego!");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/api/v1/posters/match/", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
                body: JSON.stringify(formData),
            });

            if (response.ok) {
                alert("Ogłoszenie zostało dodane!");
                setIsFormVisible(false);
            } else {
                const error = await response.json();
                alert(`Błąd: ${error.message}`);
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
                {/* Przycisk otwierający formularz */}
                <button
                    className="btn btn-primary mb-3"
                    onClick={() => setIsFormVisible(true)}
                >
                    Dodaj ogłoszenie
                </button>

                {/* Formularz dodania ogłoszenia */}
                {isFormVisible && (
                    <form onSubmit={handleSubmit} className="mb-4">
                        <div className="form-group">
                            <label>
                                Czy rankingowy:
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
                                Opis:
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
                                Data zakończenia:
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
                            Dodaj
                        </button>
                        <button
                            type="button"
                            className="btn btn-secondary ml-2"
                            onClick={() => setIsFormVisible(false)}
                        >
                            Anuluj
                        </button>
                    </form>
                )}

                {/* Wyświetlenie komponentu MatchPosters */}
                <MatchPosters />
            </main>
            <Footer />
        </div>
    );
}

export default FindGamesPage;
