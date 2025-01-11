import React from "react";
import NavBar from "../components/NavBar";
import MatchPosters from "../components/MatchPosters"; // Import komponentu MatchPosters
import Footer from "../components/Footer";

function FindGamesPage() {
    return (
        <div className="d-flex flex-column vh-100">
            <NavBar />
            <main className="flex-grow-1 p-4">
                <h1>Find Games</h1>
                {/* Wyświetlenie komponentu MatchPosters */}
                <MatchPosters />
            </main>
            <Footer />
        </div>
    );
}

export default FindGamesPage;
