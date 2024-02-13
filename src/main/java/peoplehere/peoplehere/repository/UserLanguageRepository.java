package peoplehere.peoplehere.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import peoplehere.peoplehere.domain.UserLanguage;

public interface UserLanguageRepository extends JpaRepository<UserLanguage, Long> {
    List<UserLanguage> findByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM UserLanguage ul WHERE ul.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
