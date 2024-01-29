package peoplehere.peoplehere.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByTourId(Long tourId);
}
