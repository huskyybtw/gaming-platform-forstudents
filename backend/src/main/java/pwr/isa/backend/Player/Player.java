package pwr.isa.backend.Player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

/*
 * Player entity
 * Id jest kosmetyczne
 * Wszedzie powinnismy szukac za pomoca user_id
 * TODO - W przyszlosci to naprawic
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnoreProperties
    @Schema(description = "Unique identifier of the player (cosmetic)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    @Schema(description = "User ID associated with the player", example = "404", required = true)
    private Long userId;

    @Schema(description = "Player's nickname", example = "ShadowHunter")
    private String nickname;

    @Schema(description = "Player's tagline", example = "1234")
    private String tagLine;

    @Schema(description = "OP.GG link for the player", example = "https://eune.op.gg/summoner/userName=ShadowHunter")
    private String opgg;

    @Schema(description = "Description of the player", example = "An experienced player specializing in jungle roles.")
    private String description;

    @Column(name = "last_update", nullable = false)
    @Schema(description = "Timestamp of the last player update", example = "2023-12-20T10:15:30")
    private Timestamp lastUpdate;

    @Column(nullable = true, columnDefinition = "INTEGER DEFAULT 1000")
    @Schema(description = "Player's rating", example = "1000", required = true)
    private Integer rating = 1000;

    // FROM RIOT API

    @Schema(description = "Player's PUUID (from Riot API, not editable by the user)", example = "3zX7g4f...")
    private String puuid;

    // SUMMONER-V4
    @Schema(description = "Player's Summoner ID (from Riot API, not editable by the user)", example = "AB123CDEF456...")
    private String summonerid;

    @Schema(description = "Player's account ID (from Riot API, not editable by the user)", example = "XY789Z456...")
    private String accountId;

    @Schema(description = "ID of the player's profile icon (from Riot API, not editable by the user)", example = "123")
    private int profileIconId;

    @Schema(description = "Player's summoner level (from Riot API, not editable by the user)", example = "30")
    private long summonerLevel;

}
