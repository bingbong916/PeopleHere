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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @ColumnDefault("'예약중'")
    private String status = "예약중";
}
