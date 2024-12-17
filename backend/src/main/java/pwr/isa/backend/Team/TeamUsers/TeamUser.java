package pwr.isa.backend.Team.TeamUsers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "team_user")
public class TeamUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; // Corrected to lowercase for consistency with Java conventions

    @Column(name = "team_id", nullable = false)
    Long teamId;

    @Column(name = "user_id", nullable = false)
    Long userId;
}
