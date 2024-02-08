package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {

}
