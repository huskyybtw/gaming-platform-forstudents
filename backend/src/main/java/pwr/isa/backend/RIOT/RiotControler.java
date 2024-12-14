package pwr.isa.backend.RIOT;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pwr.isa.backend.RIOT.DTO.AccountDTO;
import pwr.isa.backend.RIOT.DTO.LeagueDTO;
import pwr.isa.backend.RIOT.DTO.MatchDetailsDTO;
import pwr.isa.backend.RIOT.DTO.PlayerDTO;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("testing/riotService")
public class RiotControler {
    private RiotService riotService;

    public RiotControler(RiotService riotService) {
        this.riotService = riotService;
    }

    @GetMapping(path= "/player")
    public PlayerDTO getPlayerDTO(
            @RequestParam("username") String username,
            @RequestParam("tag") String tag) {
        return riotService.getPlayerDTO(username, tag);
    }
    @GetMapping(path= "/account")
    public AccountDTO getAccountDTO(
            @RequestParam("username") String username,
            @RequestParam("tag") String tag) {
        return riotService.getAccountDTO(username, tag);
    }

    @GetMapping(path= "/league")
    public List<LeagueDTO> getLeagueDTO(
            @RequestParam("summonerid") String summonerid) {
        return riotService.getLeagueDTO(summonerid);
    }

    @GetMapping(path= "/match")
    public MatchDetailsDTO getMatchDetailsDTO(
            @RequestParam("matchid") String matchid) {
        return riotService.getMatchDetailsDTO(matchid);
    }

    @GetMapping(path= "/matchMeta")
    public Map getMatchMeta(
            @RequestParam("matchid") String matchid) {
        return riotService.getMatchMetaData(matchid);
    }

}
