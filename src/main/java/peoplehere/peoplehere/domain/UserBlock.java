package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBlock extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blocker_id")
    private User blocker; // 차단하는 유저

    @ManyToOne
    @JoinColumn(name = "blocked_id")
    private User blocked; // 차단당하는 유저

    private String status;
}
