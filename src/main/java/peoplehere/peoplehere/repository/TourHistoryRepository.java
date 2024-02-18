package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.Tour;
import peoplehere.peoplehere.domain.TourHistory;
import peoplehere.peoplehere.domain.User;
import peoplehere.peoplehere.domain.enums.TourHistoryStatus;

import java.util.List;

public interface TourHistoryRepository extends JpaRepository<TourHistory, Long> {
    List<TourHistory> findAllByUserAndStatus(User currentUser, TourHistoryStatus tourHistoryStatus);

    List<TourHistory> findAllByTourUserAndStatus(User currentUser, TourHistoryStatus tourHistoryStatus);

    List<TourHistory> findAllByTourAndStatus(Tour tour, TourHistoryStatus tourHistoryStatus);
}
