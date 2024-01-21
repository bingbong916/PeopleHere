package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

//TODO: 사용할 데이터 넣어서 완성시키기
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Place extends BaseTimeEntity {

    public Place(String content, String imageUrl, String address, int order) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.address = address;
        this.order = order;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    private String content;

    // max : 5장, 대표 사진
    private String imageUrl;

    private String address;

    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @ColumnDefault("'일반'")
    private String status = "일반";

    //==연관관계 편의 메서드==//
    public void setTour(Tour tour) {
        this.tour = tour;
        tour.getPlaces().add(this);
    }
}
