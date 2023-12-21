package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

public class TourReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    private float score;

    @ColumnDefault("'일반'")
    private String status = "일반";
}
