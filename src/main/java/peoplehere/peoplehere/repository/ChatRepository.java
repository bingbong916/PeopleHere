package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
