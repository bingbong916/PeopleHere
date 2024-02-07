package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peoplehere.peoplehere.domain.Category;
import peoplehere.peoplehere.domain.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import peoplehere.peoplehere.domain.enums.Status;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {
    @Query("SELECT t FROM Tour t JOIN t.tourCategories tc WHERE tc.category IN :categories AND t.status = :status")
    Page<Tour> findByCategoriesInAndStatus(List<Category> categories, Status status, Pageable pageable);
    Page<Tour> findAllByStatus(Status status, Pageable pageable);
}
