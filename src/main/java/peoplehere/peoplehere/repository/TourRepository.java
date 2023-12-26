package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peoplehere.peoplehere.domain.Category;
import peoplehere.peoplehere.domain.Tour;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {
    @Query("SELECT t FROM Tour t LEFT JOIN t.tourCategories tc LEFT JOIN tc.category c WHERE c IN :categories")
    List<Tour> findByCategoryIn(List<Category> categories);
}
