package peoplehere.peoplehere.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.UserLanguage;

public interface UserLanguageRepository extends JpaRepository<UserLanguage, Long> {
    List<UserLanguage> findByUserId(Long userId);
}
