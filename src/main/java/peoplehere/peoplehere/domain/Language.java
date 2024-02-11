package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Language {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Long id;

    public Language(String koreanName, String englishName) {
        this.koreanName = koreanName;
        this.englishName = englishName;
    }

    private String koreanName;
    private String englishName;

}
