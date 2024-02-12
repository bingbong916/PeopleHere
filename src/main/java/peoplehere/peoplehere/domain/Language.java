package peoplehere.peoplehere.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    @JsonManagedReference
    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL)
    private List<UserLanguage> userLanguages = new ArrayList<>();

}
