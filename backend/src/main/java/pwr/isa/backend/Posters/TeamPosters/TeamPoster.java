package pwr.isa.backend.Posters.TeamPosters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "team_posters")
public class TeamPoster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long teamId;
    @Column(nullable = false)
    private Date CreatedAt;
    @Column(nullable = false)
    private Date DueDate;
    @Column(nullable = false)
    private Date UpdatedAt;
    private String description;
}
