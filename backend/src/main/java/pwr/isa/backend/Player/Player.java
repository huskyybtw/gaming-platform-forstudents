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
    private Long ID;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId // To mówi, że pole 'ID' jest mapowane z klucza usera
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = true)
    private String opgg;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private Timestamp lastUpdate;
}
