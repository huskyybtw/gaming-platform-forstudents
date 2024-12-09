package pwr.isa.backend.RIOT;

import pwr.isa.backend.RIOT.DTO.AccountDTO;
import pwr.isa.backend.RIOT.DTO.LeagueDTO;
import pwr.isa.backend.RIOT.DTO.MatchDetailsDTO;
import pwr.isa.backend.RIOT.DTO.SummonerDTO;

import java.util.List;
import java.util.Map;

public interface RiotService {
    PlayerDTO getPlayerDTO(String username, String tag);
    AccountDTO getAccountDTO(String username, String tag);
    SummonerDTO getSummonerDTO(String puuid);
    List<LeagueDTO> getLeagueDTO(String summonerId);
    MatchDetailsDTO getMatchDetailsDTO(String matchId);
    Map getMatchMetaData(String matchId);
}
