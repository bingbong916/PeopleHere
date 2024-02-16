package peoplehere.peoplehere.domain;

import ch.qos.logback.core.util.Loader;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import peoplehere.peoplehere.controller.dto.tour.PutTourRequest;
import peoplehere.peoplehere.domain.enums.Status;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Tour extends BaseTimeEntity {

    public Tour(String name, int time, String content) {
        this.name = name;
        this.time = time;
        this.content = content;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_id")
    private Long id;

    private String name;

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

    @JsonManagedReference
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<Place> places = new ArrayList<>();

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TourDate> tourDates = new HashSet<>();

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Wishlist> wishlists = new HashSet<>();


    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    public void addTourDate(TourDate tourDate) {
        tourDates.add(tourDate);
        tourDate.setTour(this);
    }

    public void addWishlist(Wishlist wishlist) {
        wishlists.add(wishlist);
        wishlist.setTour(this);
    }

    public void removeTourDate(TourDate tourDate) {
        tourDates.remove(tourDate);
        tourDate.setTour(null);
    }
    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public void setTourCategories(List<TourCategory> tourCategories) {
        this.tourCategories = tourCategories;
        tourCategories.forEach(tc -> tc.setTour(this));
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    //TODO: 타워 update 구현
    public Tour update(PutTourRequest putTourRequest) {
        this.name = putTourRequest.getName();
        this.time = putTourRequest.getTime();
        this.content = putTourRequest.getContent();
        return this;
    }

    //==연관관계 편의 메서드==//

    public void setUser(User user) {
        this.user = user;
        user.getTours().add(this);
    }
}
