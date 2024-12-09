package pwr.isa.backend.RIOT;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pwr.isa.backend.RIOT.DTO.LeagueDTO;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PlayerDTO {
    // ACCOUNT-V1
    String puuid;
    String gameName;
    String tagLine;

    // SUMMONER-V4
    String summonerid;
    String accountId;
    int profileIconId;
    long revisionDate;
    long summonerLevel;

    // League-V4
    LeagueDTO soloQueue;
    LeagueDTO flexQueue;
}
