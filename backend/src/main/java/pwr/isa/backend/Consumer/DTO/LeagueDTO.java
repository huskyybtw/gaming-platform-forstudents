package pwr.isa.backend.Consumer.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LeagueDTO {
    String leagueId = "unranked"; // Default value if no rank
    String queueType = "unranked"; // Default value if no rank
    String tier = "unranked"; // Default value
    String rank = null; // Default value
    String summonerId = "unranked"; // Default value
    int leaguePoints = 0; // Default value if no rank
    int wins = 0; // Default value
    int losses = 0; // Default value
    boolean hotStreak = false; // Default value
    boolean veteran = false; // Default value
    boolean freshBlood = false; // Default value
    boolean inactive = false; // Default value
    boolean miniSeries = false; // Default value
    int target = 0; // Default value
    int progress = 0; // Default value
}

