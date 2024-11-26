package pwr.isa.backend.Player;

import jakarta.persistence.*;
import lombok.*;
import pwr.isa.backend.User.User;

@Entity
@Table(name = "Player")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String opgg;

    private String description;


    // relacja OneToOne z encją User
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "ID")
    private User user;
}
