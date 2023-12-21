package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Language extends BaseTimeEntity {

    public Language(String name) {
        this.name = name;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Long id;

    private String name;

    @ColumnDefault("'일반'")
    private String status = "일반";

}
