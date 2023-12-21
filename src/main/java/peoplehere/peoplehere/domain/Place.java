package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

//TODO: 사용할 데이터 넣어서 완성시키기
public class Place extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    private String content;

    private String image_url;

    private String address;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @ColumnDefault("'일반'")
    private String status = "일반";
}
