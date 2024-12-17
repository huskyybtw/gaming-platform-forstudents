package pwr.isa.backend.Team;

import jakarta.persistence.*;
import lombok.*;
import pwr.isa.backend.Rating.TeamRating;
import pwr.isa.backend.User.User;
import pwr.isa.backend.Team.TeamUser;

import java.util.List;

@Entity
@Table(name = "teams")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name", unique = true, nullable = false)
    private String teamName;

    @Column(name = "description")
    private String description;

    private Long teamCaptain;


    // Relacja jeden-do-jeden z encją TeamRating
    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private TeamRating teamRating;
}
//{
//        "teamName": "TeamAlpha",
//        "teamCaptain": 1,
//        "userIds": [2, 3, 4],
//        "description": "A team aiming to dominate the league."
//}