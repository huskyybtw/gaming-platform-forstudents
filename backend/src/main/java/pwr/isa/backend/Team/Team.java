package pwr.isa.backend.Team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "teams")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the team", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "team_name", unique = true, nullable = false)
    @Schema(description = "Name of the team", example = "Dream Team", required = true)
    private String teamName;

    @Schema(description = "Description of the team", example = "A team focused on achieving excellence.")
    private String description;

    @Column(nullable = false)
    @Schema(description = "ID of the team captain(valid userId)", example = "42", required = true)
    private Long teamCaptain;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 1000")
    @Schema(description = "Rating of the team", example = "1000", required = true)
    private Integer rating = 1000;

    @JsonIgnore
    @Schema(description = "Flag indicating if the team is archived", example = "false", accessMode = Schema.AccessMode.WRITE_ONLY)
    private Boolean Archived;
}
