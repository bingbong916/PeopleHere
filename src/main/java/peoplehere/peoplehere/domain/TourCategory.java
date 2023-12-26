package peoplehere.peoplehere.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor
public class TourCategory extends BaseTimeEntity {

    public TourCategory(Tour tour, Category category) {
        this.tour = tour;
        this.category = category;
        this.categoryName = category.getName();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_category_id")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String categoryName;

    @ColumnDefault("'일반'")
    private String status = "일반";

    //==연관관계 편의 메서드==//
    public void setTour(Tour tour) {
        this.tour = tour;
        tour.getTourCategories().add(this);
    }

    public void setCategory(Category category) {
        this.category = category;
        category.getTourCategories().add(this);
    }
}
