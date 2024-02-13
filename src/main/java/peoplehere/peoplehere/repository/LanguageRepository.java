package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {

}
