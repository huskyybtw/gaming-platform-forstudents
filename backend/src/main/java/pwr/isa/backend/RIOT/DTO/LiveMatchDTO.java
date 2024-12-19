package pwr.isa.backend.RIOT.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LiveMatchDTO {
    private LiveMatchStatus status;
    private Long gameId;
    private Long gameStartTimestamp;
    private Long gameLengthTimestamp;

    private List<ParticipantDTO> participants;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class ParticipantDTO {
        private Integer teamId;
        private String puuid;
    }
}

