package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.SearchHistory;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
}
