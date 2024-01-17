package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.JwtBlackList;

public interface JwtBlackListRepository extends JpaRepository<JwtBlackList, Long> {

    JwtBlackList findByToken(String token);
}
