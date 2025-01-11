import Footer from "../components/Footer.tsx";
import NavBar from "../components/NavBar.tsx";
import SideBar from "../components/SideBar.tsx";
import UpdateRiotDetailsFrom from "../components/UpdateRiotDetailsFrom.tsx";
import UpdateUserDetailsFrom from "../components/UpdateUserDetailsFrom.tsx";
import MatchHistoryDisplay from "../components/MatchHistoryDisplay.tsx";
import PlayerRankDisplay from "../components/PlayerRankDisplay.tsx";
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import Cookies from "js-cookie";



function ProfilePage() {
    const [loggedInUserId, setLoggedInUserId] = useState<number | null>(null);

    useEffect(() => {
        const userId = Cookies.get("userId");

        if (userId) {
            setLoggedInUserId(parseInt(userId, 10));
        }
    }, []);

    const { id } = useParams<{ id: string }>();
    const profileId = id ? parseInt(id, 10) : parseInt(Cookies.get('userId') || '', 10);

    const isLoggedInUserProfile = loggedInUserId === profileId;

    return (
        <div className="d-flex flex-column vh-100">
            <NavBar />

            <div className="d-flex flex-grow-1">
                <main
                    className="d-grid flex-grow-1"
                    style={{
                        gridTemplateColumns: "1fr 1fr",
                    }}
                >
                    <div className="bg-light p-3 rounded">
                        {isLoggedInUserProfile && (
                            <>
                                <UpdateUserDetailsFrom />
                                <br />
                            </>
                        )}
                        <UpdateRiotDetailsFrom userId={profileId} isLoggedUser={isLoggedInUserProfile}/>
                        <br></br>
                        <PlayerRankDisplay />
                    </div>

                    <div className="bg-light p-3 rounded">
                        <MatchHistoryDisplay />
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

export default ProfilePage;
