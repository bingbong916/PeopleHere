package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peoplehere.peoplehere.domain.Category;
import peoplehere.peoplehere.domain.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {
    @Query("SELECT t FROM Tour t JOIN t.tourCategories tc WHERE tc.category IN :categories")
    Page<Tour> findByCategoriesIn(List<Category> categories, Pageable pageable);
    Page<Tour> findAll(Pageable pageable);
}
