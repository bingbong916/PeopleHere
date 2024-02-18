package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

@Entity
@Getter
@RequiredArgsConstructor
public class SearchHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String placeName;

    @Column(nullable = false)
    private String placeAddress;
}
