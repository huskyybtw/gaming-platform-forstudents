package pwr.isa.backend.Posters.UserPosters;

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
@Table(name = "user_posters")
public class UserPoster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the user poster", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "User ID associated with the poster", example = "101", required = true)
    private Long userId;

    @Column(nullable = false)
    @Schema(description = "Creation date of the poster", example = "2023-12-20T10:15:30")
    private Date CreatedAt;

    @Column(nullable = false)
    @Schema(description = "Expiration date of the poster", example = "2024-01-20T10:15:30")
    private Date DueDate;

    @Column(nullable = false)
    @Schema(description = "Last updated date of the poster", example = "2023-12-21T10:15:30")
    private Date UpdatedAt;

    @Schema(description = "Description of the poster", example = "This is a user-specific poster.")
    private String description;
}
