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
    private Long Id;
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    private String nickname;

    private String opgg;

    private String description;

    @Column(nullable = false)
    private Timestamp lastUpdate;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 1000")
    private Integer rating = 1000;;
}
//      {
//        "nickname": "Pla32yqweryOne",
//        "opgg": "http://opgg.example.com/playerone",
//        "description": "An elite player of strategy games",
//        "userId": 1
//        }