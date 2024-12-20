package pwr.isa.backend.Player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

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
    private Long id;
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    private String nickname;
    private String tagLine;
    private String opgg;
    private String description;

    @Column(name = "last_update",nullable = false)
    private Timestamp lastUpdate;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 1000")
    private Integer rating = 1000;


    // FROM RIOT API

    String puuid;

    // SUMMONER-V4
    String summonerid;
    String accountId;
    int profileIconId;
    long summonerLevel;

}
