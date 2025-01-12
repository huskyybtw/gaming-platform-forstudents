import NavBar from "../components/NavBar.tsx";
//import SideBar from "../components/SideBar.tsx";
import Footer from "../components/Footer.tsx";
import TeamAndUserPosters from "../components/TeamAndUserPosters";

function FindPlayersPage() {
    return (
        <div className="d-flex flex-column vh-100">
            <NavBar />
            <main className="flex-grow-1 p-4">
                <h1>Find Players</h1>
                {/* Wyświetlenie komponentu TeamAndUserPosters */}
                <TeamAndUserPosters />
            </main>
            <Footer />
        </div>
    );
}

export default FindPlayersPage;