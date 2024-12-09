package pwr.isa.backend.RIOT.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LeagueDTO {
    String leagueId;
    String queueType;
    String tier;
    String rank;
    String summonerId;
    String summonerName;
    int leaguePoints;
    int wins;
    int losses;
    boolean hotStreak;
    boolean veteran;
    boolean freshBlood;
    boolean inactive;
    boolean miniSeries;
    int target;
    int progress;
}
