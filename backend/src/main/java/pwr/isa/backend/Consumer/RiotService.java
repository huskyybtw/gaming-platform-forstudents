package pwr.isa.backend.Consumer;

import pwr.isa.backend.Consumer.DTO.*;

import java.util.List;
import java.util.Map;

public interface RiotService {
    PlayerDetailsDTO getPlayerDTO(String username, String tag);
    AccountDTO getAccountDTO(String username, String tag);
    SummonerDTO getSummonerDTO(String puuid);
    List<LeagueDTO> getLeagueDTO(String summonerId);
    MatchDetailsDTO getMatchDetailsDTO(String matchId);

    LiveMatchDTO getLiveMatchDTO(String puuid);

    Map getLiveMatchInfo(String puuid);

    Map getMatchMetaData(String matchId);

    List<String> getUserMatches(String puuid);
}
