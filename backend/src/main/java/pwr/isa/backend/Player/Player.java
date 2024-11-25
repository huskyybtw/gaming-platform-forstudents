package pwr.isa.backend.Player;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Player")
public class Player {

    @Id
    private Long userId;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String opgg;

    private String description;
}
