package pwr.isa.backend.Rating;

import jakarta.persistence.*;

@Entity
@Table(name = "Team_Rating")
public class TeamRating {

    @Id
    @Column(name = "Team_id")
    private Long teamId;

    @Column(name = "Rating", nullable = false)
    private Long rating;

    // Gettery i Settery
    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }
}
