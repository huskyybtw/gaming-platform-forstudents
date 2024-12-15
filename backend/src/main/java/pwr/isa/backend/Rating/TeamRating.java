package pwr.isa.backend.Rating;

import jakarta.persistence.*;
import lombok.*;
import pwr.isa.backend.Team.Team;

@Entity
@Table(name = "team_rating")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "team_id", nullable = false, unique = true)
    private Team team;

    @Column(nullable = false)
    private Integer rating;
}
