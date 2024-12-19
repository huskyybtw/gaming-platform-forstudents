package pwr.isa.backend.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pwr.isa.backend.Consumer.DTO.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/*
    * Service that collects data from RIOT API and binds it into PlayerDTO
    * For now only support EUNE region
    *
    * TODO - refactor names
    * TODO - add retrying for failed requests
    * TODO - add validation
    * TODO ? - add support for multiple regions
 */

@Service
public class RiotServiceImpl implements RiotService {
    private final WebClient EUROPE_WEB_CLIENT;
    private final WebClient EUNE_WEB_CLIENT;
    private final String API_KEY;

    public RiotServiceImpl(@Value("${app.RIOT_API_KEY}") String API_KEY) {
        this.API_KEY = API_KEY;
        this.EUROPE_WEB_CLIENT = WebClient.builder()
                .baseUrl("https://europe.api.riotgames.com")
                .build();
        this.EUNE_WEB_CLIENT = WebClient.builder()
                .baseUrl("https://eun1.api.riotgames.com")
                .build();
    }
    @Override
    public PlayerDetailsDTO getPlayerDTO(String username, String tag) {
        AccountDTO accountDTO = getAccountDTO(username, tag);
        SummonerDTO summonerDTO = getSummonerDTO(accountDTO.getPuuid());
        List<LeagueDTO> leagueDTO = getLeagueDTO(summonerDTO.getSummonerId());

        return PlayerDetailsDTO.builder()
                .gameName(username)
                .tagLine(tag)
                .accountId(summonerDTO.getAccountId())
                .puuid(summonerDTO.getPuuid())
                .summonerid(summonerDTO.getSummonerId())
                .gameName(accountDTO.getGameName())
                .tagLine(accountDTO.getTagLine())
                .profileIconId(summonerDTO.getProfileIconId())
                .revisionDate(summonerDTO.getRevisionDate())
                .summonerLevel(summonerDTO.getSummonerLevel())
                .soloQueue(leagueDTO.get(0))
                .flexQueue(leagueDTO.get(1))
                .build();
    }

    @Override
    public AccountDTO getAccountDTO(String username, String tag) {
        return EUROPE_WEB_CLIENT.get()
                .uri("/riot/account/v1/accounts/by-riot-id/" + username + "/" + tag + "?api_key=" + API_KEY)
                .retrieve()
                .bodyToMono(AccountDTO.class)
                .block();
    }

    @Override
    public SummonerDTO getSummonerDTO(String puuid) {
        return EUNE_WEB_CLIENT.get()
                .uri("/lol/summoner/v4/summoners/by-puuid/" + puuid + "?api_key=" + API_KEY)
                .retrieve()
                .bodyToMono(SummonerDTO.class)
                .block();
    }

    @Override
    public List<LeagueDTO> getLeagueDTO(String summonerId) {
        return EUNE_WEB_CLIENT.get()
                .uri("lol/league/v4/entries/by-summoner/" + summonerId + "?api_key=" + API_KEY)
                .retrieve()
                .bodyToFlux(LeagueDTO.class)
                .collectList()
                .block();
    }

    @Override
    public MatchDetailsDTO getMatchDetailsDTO(String matchid) {
        MatchDetailsDTO matchDetailsDTO = new MatchDetailsDTO();

        Map<String, Object> matchData = getMatchMetaData(matchid);

        matchDetailsDTO.setMatchId(matchid);

        Map<String, Object> info = (Map<String, Object>) matchData.get("info");

        matchDetailsDTO.setEndOfGameResult((String) info.get("endOfGameResult"));
        matchDetailsDTO.setGameDuration((Integer) info.get("gameDuration"));
        matchDetailsDTO.setGameVersion((String) info.get("gameVersion"));

        List<Map<String, Object>> participantInfo = (List<Map<String, Object>>) info.get("participants");
        List<ParticipantDTO> participants = new ArrayList<ParticipantDTO>();

        for (Map<String, Object> participant : participantInfo) {
            ParticipantDTO participantDTO = new ParticipantDTO();

            participantDTO.setPuuid((String) participant.get("puuid"));
            participantDTO.setPlayerId((String) participant.get("summonerName"));
            participantDTO.setChampionId((int) participant.get("championId"));

            participantDTO.setChampionName((String) participant.get("championName"));
            participantDTO.setChampionLevel((int) participant.get("champLevel"));

            participantDTO.setKills((int) participant.get("kills"));
            participantDTO.setDeaths((int) participant.get("deaths"));
            participantDTO.setAssists((int) participant.get("assists"));
            participantDTO.setTotalMinionsKilled((int) participant.get("totalMinionsKilled"));

            participantDTO.setSummoner1Id((int) participant.get("summoner1Id"));
            participantDTO.setSummoner2Id((int) participant.get("summoner2Id"));

            participantDTO.setItem0((int) participant.get("item0"));
            participantDTO.setItem1((int) participant.get("item1"));
            participantDTO.setItem2((int) participant.get("item2"));
            participantDTO.setItem3((int) participant.get("item3"));
            participantDTO.setItem4((int) participant.get("item4"));
            participantDTO.setItem5((int) participant.get("item5"));
            participantDTO.setItem6((int) participant.get("item6"));

            participantDTO.setRole((String) participant.get("individualPosition"));

            participantDTO.setTeamId((int) participant.get("teamId"));
            participantDTO.setWin((boolean) participant.get("win"));

            // Extract the perks
            Map<String, Object> perks = (Map<String, Object>) participant.get("perks");
            List<Map<String, Object>> styles = (List<Map<String, Object>>) perks.get("styles");

            for (Map<String, Object> style : styles) {

                if ("primaryStyle".equals(style.get("description"))) {
                    List<Map<String, Object>> selections = (List<Map<String, Object>>) style.get("selections");
                    Map<String, Object> perk0 = selections.get(0);
                    participantDTO.setKeyStoneId((int) perk0.get("perk"));
                }

                // Extract Substyle style
                if ("subStyle".equals(style.get("description"))) {
                    int subStyle = (int) style.get("style");
                    participantDTO.setSecondaryStyleId(subStyle);
                }
            }

            participants.add(participantDTO);
        }
        matchDetailsDTO.setParticipant(participants);


        List<Map<String,Object>> teams = (List<Map<String, Object>>) info.get("teams");
        for (Map<String, Object> team : teams) {
            if ((boolean) team.get("win") == true) {
                matchDetailsDTO.setWinner((int) team.get("teamId"));
            }
        }

        return matchDetailsDTO;
    }

    @Override
    public LiveMatchDTO getLiveMatchDTO(String puuid){
        Map response = getLiveMatchInfo(puuid);
        LiveMatchDTO dto = new LiveMatchDTO();
        if (response.containsKey("status")) {
            dto.setStatus(LiveMatchStatus.NOT_FOUND);
            return dto;
        }

        dto.setStatus(LiveMatchStatus.IN_PROGRESS);
        dto.setGameId(((Number) response.get("gameId")).longValue()); // Convert to Long
        dto.setGameStartTimestamp(((Number) response.get("gameStartTime")).longValue()); // Convert to Long
        dto.setGameLengthTimestamp(((Number) response.get("gameLength")).longValue()); // Convert to Long

        List<Map<String, Object>> participants = (List<Map<String, Object>>) response.get("participants");
        List<LiveMatchDTO.ParticipantDTO> participantDTOs = participants.stream()
                .map(participant -> {
                    LiveMatchDTO.ParticipantDTO participantDTO = new LiveMatchDTO.ParticipantDTO();
                    participantDTO.setTeamId((Integer) participant.get("teamId"));
                    participantDTO.setPuuid((String) participant.get("puuid"));
                    return participantDTO;
                })
                .collect(Collectors.toList());
        dto.setParticipants(participantDTOs);

        return dto;
    }
    @Override
    public Map getLiveMatchInfo(String puuid) {
        return EUNE_WEB_CLIENT.get()
                .uri("/lol/spectator/v5/active-games/by-summoner/" + puuid + "?api_key=" + API_KEY)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(Map.class); // Normal response
                    } else {
                        return response.bodyToMono(Map.class) // Error body
                                .defaultIfEmpty(Map.of("status", Map.of(
                                        "message", "No content",
                                        "status_code", response.statusCode().value()
                                )));
                    }
                })
                .block(); // Blocking to return the result immediately (sync method)
    }

    @Override
    public Map getMatchMetaData(String matchid) {
        return EUROPE_WEB_CLIENT.get()
                .uri("/lol/match/v5/matches/" + matchid + "?api_key=" + API_KEY)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    @Override
    public List<String> getUserMatches(String puuid){
        return EUROPE_WEB_CLIENT.get()
                .uri("/lol/match/v5/matches/by-puuid/" + puuid + "/ids?start=0&count=10&api_key=" + API_KEY)
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();
    }





}
