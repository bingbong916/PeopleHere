package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.Tour;

public interface TourRepository extends JpaRepository<Tour, Long> {

}
