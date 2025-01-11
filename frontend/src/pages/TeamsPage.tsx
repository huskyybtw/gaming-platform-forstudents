import NavBar from "../components/NavBar.tsx";
import SideBar from "../components/SideBar.tsx";
import Footer from "../components/Footer.tsx";

function TeamsPage(){
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
                    <h1>Teams</h1>
                    <div>
                        <button className={"btn btn-primary"}>
                            Create Team
                        </button>
                    </div>
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
    );
}

export default TeamsPage;