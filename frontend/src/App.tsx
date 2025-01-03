import {BrowserRouter as Router, Route, Routes} from "react-router-dom";

import './styles/App.css';
import LoginPage from "./pages/LoginPage.tsx";
import LoggedUserPage from "./pages/LoggedUserPage.tsx";
import FindGamesPage from "./pages/FindGamesPage.tsx";
import FindPlayersPage from "./pages/FindPlayersPage.tsx";
import LeaderBoardPage from "./pages/LeaderBoardPage.tsx";

function App() {
  return <Router>
    <Routes>
      <Route path="/" element={<LoginPage />} />
      <Route path="/profile" element={<LoggedUserPage />} />
      <Route path="/games" element={<FindGamesPage />} />
      <Route path="/players" element={<FindPlayersPage />} />
      <Route path="/leaderboards" element={<LeaderBoardPage />} />
    </Routes>
  </Router>
}

export default App
