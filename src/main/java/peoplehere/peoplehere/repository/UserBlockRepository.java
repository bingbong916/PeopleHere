package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.UserBlock;

public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {
}
