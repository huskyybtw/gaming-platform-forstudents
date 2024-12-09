package pwr.isa.backend.RIOT.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ParticipantDTO {
    String puuid;
    String playerId;
    int championId;
    String championName;
    int championLevel;

    int kills;
    int deaths;
    int assists;
    int totalMinionsKilled;

    int summoner1Id;
    int summoner2Id;

    int keyStoneId;
    int secondaryStyleId;

    int item0;
    int item1;
    int item2;
    int item3;
    int item4;
    int item5;
    int item6;

    String role;

    int teamId;
    boolean win;
}
