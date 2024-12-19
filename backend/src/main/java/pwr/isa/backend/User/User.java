package pwr.isa.backend.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @Schema(description = "Unique identifier of the user", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long ID;

    @Column(unique = true, nullable = false)
    @Schema(description = "Email address of the user", example = "studentx@student.pwr.edu.pl", required = true)
    private String email;

    @Column(nullable = false)
    @Schema(description = "Password of the user", example = "password", required = true, accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Role of the user", example = "USER", required = true)
    private UserRole role;

    @Builder.Default
    @Schema(description = "Flag indicating if the user is verified by email", example = "false", required = true)
    private boolean enabled = false;
}
