import {BrowserRouter as Router, Route, Routes} from "react-router-dom";

import './styles/App.css';
import LoginPage from "./pages/LoginPage.tsx";
import ProfilePage from "./pages/ProfilePage.tsx";
import FindGamesPage from "./pages/FindGamesPage.tsx";
import FindPlayersPage from "./pages/FindPlayersPage.tsx";
import LeaderBoardPage from "./pages/LeaderBoardPage.tsx";
import AccessForbiddenPage from "./pages/AccessForbiddenPage.tsx";
import TeamsPage from "./pages/TeamsPage.tsx";
import Cookies from 'js-cookie';

function App() {
  Cookies.set('token', 'test');
  Cookies.set('userId', '1');  // Set the userId in the cookie

  return <Router>
    <Routes>
      <Route path="/" element={<LoginPage />} />
      <Route path="/profile/:id" element={<ProfilePage />} />
      <Route path="/games" element={<FindGamesPage />} />
      <Route path="/players" element={<FindPlayersPage />} />
      <Route path="/teams" element={<TeamsPage />} />
      <Route path="/leaderboards" element={<LeaderBoardPage />} />
      <Route path="/forbidden" element={<AccessForbiddenPage/>} />
    </Routes>
  </Router>
}

export default App
