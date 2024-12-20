package pwr.isa.backend.Team;

import jakarta.persistence.*;
import lombok.*;

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

    private String description;

    @Column(nullable = false)
    private Long teamCaptain;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 1000")
    private Integer rating = 1000;
}
