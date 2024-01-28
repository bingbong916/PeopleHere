package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
