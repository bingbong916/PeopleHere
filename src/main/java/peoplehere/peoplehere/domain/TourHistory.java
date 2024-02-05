package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

@Entity
@Getter
public class TourHistory extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @ColumnDefault("'예약중'")
    private String status = "예약중";

    //==연관관계 편의 메서드==//
    public void setUser(User user) {
        this.user = user;
        user.getTourHistories().add(this);
    }

    public void setTour(Tour tour) {
        this.tour = tour;
        tour.getTourHistories().add(this);
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
