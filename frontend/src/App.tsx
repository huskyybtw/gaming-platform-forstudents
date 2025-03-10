import {BrowserRouter as Router, Route, Routes} from "react-router-dom";

import './styles/App.css';
import LoginPage from "./pages/LoginPage.tsx";
import ProfilePage from "./pages/ProfilePage.tsx";
import FindGamesPage from "./pages/FindGamesPage.tsx";
import FindPlayersPage from "./pages/FindPlayersPage.tsx";
import LeaderBoardPage from "./pages/LeaderBoardPage.tsx";
import AccessForbiddenPage from "./pages/AccessForbiddenPage.tsx";
import TeamsPage from "./pages/TeamsPage.tsx";
import TeamDetailsPage from "./pages/TeamDetailsPage.tsx";
import MatchDetailsPage from "./pages/MatchDetailsPage.tsx";

function App() {

  return <Router>
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/profile/:id" element={<ProfilePage />} />
      <Route path="/games" element={<FindGamesPage />} />
      <Route path="/players" element={<FindPlayersPage />} />
      <Route path="/teams" element={<TeamsPage />} />
      <Route path="/teams/:teamid" element={<TeamDetailsPage />} />
      <Route path="/" element={<LeaderBoardPage />} />
      <Route path="/forbidden" element={<AccessForbiddenPage/>} />
      <Route path="/games/:id" element={<MatchDetailsPage/>} />

    </Routes>
  </Router>
}

export default App
