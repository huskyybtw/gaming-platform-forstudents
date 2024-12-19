package pwr.isa.backend.GameSystem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pwr.isa.backend.GameSystem.MatchParticipants.MatchParticipant;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GameHistoryDTO {
    GameHistory gameHistory;
    List<MatchParticipant> matchParticipants;
}
