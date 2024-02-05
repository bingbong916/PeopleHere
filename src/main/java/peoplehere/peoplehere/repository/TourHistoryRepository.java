package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.TourHistory;

public interface TourHistoryRepository extends JpaRepository<TourHistory, Long> {
}
