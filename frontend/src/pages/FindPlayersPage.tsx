import NavBar from "../components/NavBar.tsx";
import Footer from "../components/Footer.tsx";
import TeamAndUserPosters from "../components/TeamAndUserPosters.tsx";
import "../styles/FindPlayersPage.css";

function FindPlayersPage() {
    return (
        <div className="page-container">
            <NavBar />
            <main className="content-container">
                <div className="page-header">
                    <h1>Find Players</h1>
                    <p className="page-description">
                        Explore available players and posters for team collaboration.
                    </p>
                </div>
                <TeamAndUserPosters />
            </main>
            <Footer />
        </div>
    );
}

export default FindPlayersPage;
