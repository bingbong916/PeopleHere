package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.TourDate;

import java.util.List;

public interface TourDateRepository extends JpaRepository<TourDate, Long> {
    List<TourDate> findByTourId(Long tourId);
}
