package pwr.isa.backend.Team;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String teamName;

    @ElementCollection
    @CollectionTable(name = "team_users", joinColumns = @JoinColumn(name = "team_id"))
    @Column(name = "user_id")
    private List<Long> userIds;

    @Column(nullable = false)
    private Long teamCapitan;

    @Column(length = 500)
    private String description;
}
