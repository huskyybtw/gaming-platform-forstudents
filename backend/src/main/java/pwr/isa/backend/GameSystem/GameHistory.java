package pwr.isa.backend.GameSystem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "game_history")
public class GameHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_history_id_seq")
    private Long Id;

    private Long matchId;

    private String riotMatchId;
    private Date startMatchDate;
    private Date endMatchDate;

    @Enumerated(EnumType.STRING)
    private MatchStatus matchStatus;
    private int winner;

    @Column(columnDefinition = "json")
    private String jsonData;
}
