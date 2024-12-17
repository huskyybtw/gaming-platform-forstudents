package pwr.isa.backend.Rating;

import jakarta.persistence.*;
import lombok.*;
import pwr.isa.backend.Player.Player;

@Entity
@Table(name = "player_rating")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "player_id", nullable = false, unique = true)
    private Player player;

    @Column(nullable = false)
    private Integer rating;
}

