package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import lombok.Getter;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

@Entity
@Getter
public class UserLanguage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;
}
