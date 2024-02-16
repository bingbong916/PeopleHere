package peoplehere.peoplehere.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.locationtech.jts.geom.Point;
import peoplehere.peoplehere.controller.dto.place.PlaceInfoDto;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

//TODO: 사용할 데이터 넣어서 완성시키기
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Place extends BaseTimeEntity {

    public Place(String content, List<String> imageUrls, String address, Point latLng, int order) {
        this.content = content;
        this.imageUrls = imageUrls;
        this.address = address;
        this.latLng = latLng;
        this.order = order;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    private String content;

    @ElementCollection
    @CollectionTable(name = "place_images", joinColumns = @JoinColumn(name = "place_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    private String address;

    // TODO: 위도 경도 추가하기
    @Column(columnDefinition = "POINT")
    private Point latLng;

    @Column(name = "`order`")
    private int order;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @ColumnDefault("'일반'")
    private String status = "일반";

    public void addImageUrls(String imageUrl){
        imageUrls.add(imageUrl);
    }

    //==연관관계 편의 메서드==//
    public void setTour(Tour tour) {
        this.tour = tour;
        tour.getPlaces().add(this);
    }

    public void update(PlaceInfoDto placeInfoDto) {
        if (placeInfoDto.getContent() != null) {
            this.content = placeInfoDto.getContent();
        }
        if (placeInfoDto.getImageUrls() != null) {
            this.imageUrls = placeInfoDto.getImageUrls();
        }
        if (placeInfoDto.getAddress() != null) {
            this.address = placeInfoDto.getAddress();
        }
    }
}
