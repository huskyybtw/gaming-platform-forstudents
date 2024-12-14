package pwr.isa.backend.RIOT.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MatchDetailsDTO {
    String matchId;

    String endOfGameResult;
    Integer gameDuration;
    String gameVersion;
    int winner;
    List<ParticipantDTO> participant;
}
