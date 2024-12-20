package pwr.isa.backend.GameSystem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

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
    @Schema(description = "Unique identifier of the game history entry", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long Id;

    @Schema(description = "ID of the match associated with this history entry", example = "101", required = true)
    private Long matchId;

    @Schema(description = "Riot's match identifier", example = "EUW1_1234567890")
    private String riotMatchId;

    @Schema(description = "Start date and time of the match", example = "2023-12-20T10:15:30")
    private Date startMatchDate;

    @Schema(description = "End date and time of the match", example = "2023-12-20T11:45:30")
    private Date endMatchDate;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Status of the match", example = "COMPLETED", required = true)
    private MatchStatus matchStatus;

    @Schema(description = "ID of the winning team in RIOT's format 100 or 200", example = "100", required = true)
    private int winner;

    @Column(columnDefinition = "json")
    @Schema(description = "Match Meta Data details stored as JSON", accessMode =Schema.AccessMode.READ_ONLY)
    private String jsonData;
}
