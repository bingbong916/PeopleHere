package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

@Entity
public class Tour extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
