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
//relację między zespołem (Team) a jego członkami (User).
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name", unique = true, nullable = false)
    private String teamName;

    @Column(name = "description")
    private String description;

    // Relacja wiele-do-jednego z encją User (kapitan zespołu)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_captain_id", nullable = false)
    private User teamCaptain;

    // Relacja jeden-do-wiele z encją TeamUser (członkowie zespołu)
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamUser> teamMembers;

    // Relacja jeden-do-jeden z encją TeamRating
    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private TeamRating teamRating;
}
//{
//        "teamName": "TeamAlpha",
//        "teamCapitan": 1,
//        "userIds": [2, 3, 4],
//        "description": "A team aiming to dominate the league."
//        }
