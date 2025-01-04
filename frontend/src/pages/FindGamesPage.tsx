import NavBar from "../components/NavBar.tsx";
import SideBar from "../components/SideBar.tsx";
import Footer from "../components/Footer.tsx";

function FindGamesPage (){
    return (
        <div className="d-flex flex-column vh-100">
            <NavBar/>

            <div className="d-flex flex-grow-1">
                <main
                    className="d-grid flex-grow-1"
                    style={{
                        gridTemplateColumns: "1fr 1fr",
                    }}
                >
                    <h1>Find Games</h1>
                </main>


                <aside
                    className="bg-light"
                    style={{width: "200px", minWidth: "200px"}}
                >
                    <SideBar/>
                </aside>
            </div>

            <Footer/>
        </div>
    )
}

export default FindGamesPage;