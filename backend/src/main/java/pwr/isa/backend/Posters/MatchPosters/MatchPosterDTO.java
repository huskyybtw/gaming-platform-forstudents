package pwr.isa.backend.Posters.MatchPosters;

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
public class MatchPosterDTO {
    MatchPoster matchPoster;
    List<MatchParticipant> participants;
}
