package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

//TODO: 차단하기 도메인 설계
public class UserBlock extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Long id;

    @ColumnDefault("'일반'")
    private String status = "일반";
}
