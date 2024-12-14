package pwr.isa.backend.GameHistory.MatchParticipants;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pwr.isa.backend.GameHistory.GameHistory;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "match_participant")
public class MatchParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "match_participant_id_seq")
    Long id;
    Long userId;
    Long matchId;
    Long teamId;
    int riot_team_number;
}
