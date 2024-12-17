package pwr.isa.backend.Rating;

import pwr.isa.backend.Player.Player;
import pwr.isa.backend.Team.Team;

import java.util.List;

public interface RatingService {


    // PLAYER RATING METHODS
    Player updatePlayerRating(Long userId, Integer difference);
    Integer getPlayerRating(Long userId);
    List<Player> getBestPlayers(int limit, int offset);

    // TEAM RATING METHODS
    Team updateTeamRating(Long teamId, Integer difference);
    Integer getTeamRating(Long teamId);
    List<Team> getBestTeams(int limit, int offset);
}
