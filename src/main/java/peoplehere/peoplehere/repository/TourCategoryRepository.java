package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.TourCategory;

public interface TourCategoryRepository extends JpaRepository<TourCategory, Long> {
}
