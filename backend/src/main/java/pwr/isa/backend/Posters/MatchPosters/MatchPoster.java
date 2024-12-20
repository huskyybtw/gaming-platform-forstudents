package pwr.isa.backend.Posters.MatchPosters;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "match_posters")
public class MatchPoster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long ownerId;
    @Column(nullable = false)
    private Date CreatedAt;
    @Column(nullable = false)
    private Date DueDate;
    @Column(nullable = false)
    private Date UpdatedAt;
    @Column(nullable = false)
    private Boolean ranked;
    private String description;
    @JsonIgnore
    private Boolean archived;
}
