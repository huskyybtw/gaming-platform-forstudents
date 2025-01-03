package pwr.isa.backend.Consumer.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PlayerDetailsDTO {
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
