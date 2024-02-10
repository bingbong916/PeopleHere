package peoplehere.peoplehere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peoplehere.peoplehere.domain.Wishlist;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
}
