package pwr.isa.backend.Posters.MatchPosters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "match_posters")
public class MatchPoster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the match poster", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Owner ID associated with the match poster", example = "303", required = true)
    private Long ownerId;

    @Column(nullable = false)
    @Schema(description = "Creation date of the match poster", example = "2023-12-20T10:15:30")
    private Date CreatedAt;

    @Column(nullable = false)
    @Schema(description = "Expiration date of the match poster", example = "2024-01-20T10:15:30")
    private Date DueDate;

    @Column(nullable = false)
    @Schema(description = "Last updated date of the match poster", example = "2023-12-21T10:15:30")
    private Date UpdatedAt;

    @Column(nullable = false)
    @Schema(description = "Indicates whether the match is ranked", example = "true", required = true)
    private Boolean ranked;

    @Schema(description = "Description of the match poster", example = "This is a competitive match poster.")
    private String description;

    @JsonIgnore
    @Schema(description = "Flag indicating if the match poster is archived", example = "false", accessMode = Schema.AccessMode.WRITE_ONLY)
    private Boolean archived;
}
