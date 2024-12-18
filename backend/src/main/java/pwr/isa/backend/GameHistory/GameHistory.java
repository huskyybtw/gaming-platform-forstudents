package pwr.isa.backend.GameHistory;

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
public class GameHistory implements Comparable<GameHistory> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_history_id_seq")
    private Long Id;

    private Long matchId;
    private Date startDate;
    private Date endOfMatchDate;

    @Enumerated(EnumType.STRING)
    private MatchStatus matchStatus;
    private int winner;

    @Column(columnDefinition = "json")
    private String jsonData;

    @Override
    public int compareTo(GameHistory o) {
        return getEndOfMatchDate().compareTo(o.getEndOfMatchDate());
    }
}
