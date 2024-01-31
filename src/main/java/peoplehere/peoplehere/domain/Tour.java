package peoplehere.peoplehere.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import peoplehere.peoplehere.controller.dto.tour.PutTourRequest;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Tour extends BaseTimeEntity {

    public Tour(String name, int time, String imageUrl, String content) {
        this.name = name;
        this.time = time;
        this.imageUrl = imageUrl;
        this.content = content;
        // TODO: 필드 수정 (코스 순서)
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_id")
    private Long id;

    private String name;

    private Date startDate;

    private int time;

    private String imageUrl;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonManagedReference
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<TourCategory> tourCategories = new ArrayList<>();

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<TourReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<TourHistory> tourHistories = new ArrayList<>();

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<Chat> chats = new ArrayList<>();

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<Place> places = new ArrayList<>();

    @ColumnDefault("'닫힘'")
    private String status = "닫힘";

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    public void setPlace(List<Place> places) {
        this.places = places;
    }

    public void setTourCategories(List<TourCategory> tourCategories) {
        this.tourCategories = tourCategories;
        tourCategories.forEach(tc -> tc.setTour(this));
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //TODO: 타워 update 구현
    public Tour update(PutTourRequest putTourRequest) {
        this.name = putTourRequest.getName();
        this.startDate = putTourRequest.getStartDate();
        this.time = putTourRequest.getTime();
        this.imageUrl = putTourRequest.getImageUrl();
        this.content = putTourRequest.getContent();
        return this;
    }

    //==연관관계 편의 메서드==//

    public void setUser(User user) {
        this.user = user;
        user.getTours().add(this);
    }
}
