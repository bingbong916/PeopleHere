package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String password;

    private String name;

    private String gender;

    private boolean leader;

    private String imageUrl;

    private String content;

    public User(String email, String password, String name, String gender, boolean leader, String imageUrl, String content) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.leader = leader;
        this.imageUrl = imageUrl;
        this.content = content;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserLanguage> languages = new ArrayList<>();

    //투어 리더 입장에서 만든 투어 조회하기 위한 양방향 매핑. 여행객이 이용한 투어를 조회하려면 TourHistory테이블 조인해야한다.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Tour> tours = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Chat> chats = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TourHistory> tourHistories = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TourReview> tourReviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();


    @ColumnDefault("'일반'")
    private String status = "일반";

    public void setStatus(String status) {
        this.status = status;
    }
}
