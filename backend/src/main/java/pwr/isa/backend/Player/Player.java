package pwr.isa.backend.Player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import pwr.isa.backend.User.User;
import pwr.isa.backend.Rating.PlayerRating;
import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "player")
public class Player {

    @Id
    private Long id;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;


    @OneToOne()//fetch = FetchType.LAZY, optional = false
    @MapsId // 'ID' jest mapowane z klucza usera
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = true)
    private String opgg;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private Timestamp lastUpdate;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private PlayerRating playerRating;
}
