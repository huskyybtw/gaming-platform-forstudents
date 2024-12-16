package pwr.isa.backend.Rating;

import jakarta.persistence.*;

@Entity
@Table(name = "Player_Rating")
public class PlayerRating {

    @Id
    @Column(name = "User_id")
    private Long userId;

    @Column(name = "Rating", nullable = false)
    private Long rating;

    // Gettery i Settery
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }
}
//{
//        "userId": 1,
//        "rating": 900
//        }