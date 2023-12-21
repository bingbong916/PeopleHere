package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

import java.sql.Date;

@Entity
@Getter
public class Tour extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_id")
    private Long id;

    private String name;

    private int budget;

    private Date start_date;

    private int time;

    private String image_url;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ColumnDefault("'닫힘'")
    private String status = "닫힘";
}
