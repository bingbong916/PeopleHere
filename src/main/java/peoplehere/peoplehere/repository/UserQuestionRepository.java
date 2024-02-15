package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.Question;
import peoplehere.peoplehere.domain.User;
import peoplehere.peoplehere.domain.UserQuestion;

import java.util.List;
import java.util.Optional;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
    List<UserQuestion> findByUserId(Long userId);

    Optional<UserQuestion> findByUserAndQuestion(User user, Question question);
}
