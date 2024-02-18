package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;


@Entity
@Getter
@RequiredArgsConstructor
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id")
    private User reviewee;

    public void setContent(String content) {
        this.content = content;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public void setReviewee(User reviewee) {
        this.reviewee = reviewee;
    }
}
