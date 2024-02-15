package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
