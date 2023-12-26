package peoplehere.peoplehere.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;
import peoplehere.peoplehere.dto.tour.TourModifyDto;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Tour extends BaseTimeEntity {

    public Tour(String name, int budget, int time, String imageUrl, String content) {
        this.name = name;
        this.budget = budget;
        this.time = time;
        this.imageUrl = imageUrl;
        this.content = content;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_id")
    private Long id;

    private String name;

    private int budget;

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

    public void changeStatusToDelete() {
        this.status = "삭제";
    }

    //TODO: 타워 update 구현
    public Tour update(TourModifyDto tourModifyDto) {
        return this;
    }

    //==연관관계 편의 메서드==//

    public void setUser(User user) {
        this.user = user;
        user.getTours().add(this);
    }
}
