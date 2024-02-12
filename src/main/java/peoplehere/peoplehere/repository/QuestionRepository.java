package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findByName(String name);
    List<Question> findByNameIn(List<String> names);
}
