package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import peoplehere.peoplehere.domain.enums.TourHistoryStatus;
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
    @JoinColumn(name = "tour_date_id")
    private TourDate tourDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @Enumerated(EnumType.STRING)
    private TourHistoryStatus status = TourHistoryStatus.RESERVED;

    //==연관관계 편의 메서드==//
    public void setUser(User user) {
        this.user = user;
        user.getTourHistories().add(this);
    }

    public void setTourDate(TourDate tourDate) {
        this.tourDate = tourDate;
        tourDate.getTourHistories().add(this);
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public void setStatus(TourHistoryStatus status) {
        this.status = status;
    }
}
