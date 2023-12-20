package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import lombok.Getter;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    //TODO: 인증 단계 필요
    private String email;

    private String password;

    private String name;

    private String gender;

    private boolean leader;

    private String image;

    private String content;

    @OneToMany(mappedBy = "user")
    private List<UserLanguage> languages = new ArrayList<>();

    //투어 리더 입장에서 만든 투어 조회하기 위한 매핑. 여행객이 이용한 투어를 조회하려면 TourHistory테이블 조인해야한다.
    @OneToMany(mappedBy = "user")
    private List<Tour> tours = new ArrayList<>();
}
