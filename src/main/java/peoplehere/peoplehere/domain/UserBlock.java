package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

@Entity
@Getter
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setBlocker(User blocker) {
        this.blocker = blocker;
    }

    public void setBlocked(User blocked) {
        this.blocked = blocked;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
