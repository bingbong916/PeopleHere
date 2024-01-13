package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

@Entity
@Getter
@RequiredArgsConstructor
public class JwtBlackList extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public JwtBlackList(String token) {
        this.token = token;
    }

    private String token;
}
