package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Language {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Long id;

    public Language(String englishName, String koreanName, String isoCode) {
        this.englishName = englishName;
        this.koreanName = koreanName;
        this.isoCode = isoCode;
    }

    private String englishName;

    private String koreanName;

    private String isoCode;

}
