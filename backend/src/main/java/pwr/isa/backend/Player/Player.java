package pwr.isa.backend.Player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import pwr.isa.backend.User.User;

import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnoreProperties
    private Long ID;

    Long UserId;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = true)
    private String opgg;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private Timestamp lastUpdate;
}
