package pwr.isa.backend.GameHistory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pwr.isa.backend.GameHistory.MatchParticipants.MatchParticipant;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GameHistoryDTO {
    GameHistory gameHistory;
    List<MatchParticipant> matchParticipants;
}
