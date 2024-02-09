package peoplehere.peoplehere.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import peoplehere.peoplehere.domain.enums.TourDateStatus;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class TourDate extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_date_id")
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    private LocalDate date;
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    private TourDateStatus status = TourDateStatus.BLOCKED;

    @OneToMany(mappedBy = "tourDate", cascade = CascadeType.ALL)
    private List<TourHistory> tourHistories = new ArrayList<>();

    // 날짜와 시간을 선택적으로 반환하는 메서드
    public LocalDateTime getDateTime() {
        if (date != null) {
            return (time != null) ? LocalDateTime.of(date, time) : LocalDateTime.of(date, LocalTime.MIDNIGHT);
        }
        return null;
    }

    // 날짜와 시간을 함께 설정하는 메서드
    public void setDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            this.date = dateTime.toLocalDate();
            this.time = dateTime.toLocalTime() == LocalTime.MIDNIGHT ? null : dateTime.toLocalTime();
        }
    }

    // 상태 업데이트 메서드
    public void makeAvailable() {
        this.status = TourDateStatus.AVAILABLE;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public void setStatus(TourDateStatus status) {
        this.status = status;
    }

    public void addTourHistory(TourHistory tourHistory) {
        this.tourHistories.add(tourHistory);
        tourHistory.setTourDate(this);
    }

    public void removeTourHistory(TourHistory tourHistory) {
        this.tourHistories.remove(tourHistory);
        tourHistory.setTourDate(null);
    }
}
