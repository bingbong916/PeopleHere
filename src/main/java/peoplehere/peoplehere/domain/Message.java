package peoplehere.peoplehere.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import peoplehere.peoplehere.domain.util.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor

public class Message extends BaseTimeEntity {
    public Message(User user, Chat chat, String content) {
        this.user = user;
        this.chat = chat;
        this.content = content;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    private String content;

    @ColumnDefault("'일반'")
    private String status = "일반";

    //==연관관계 편의 메서드==//
    public void setUser(User user) {
        this.user = user;
        user.getMessages().add(this);
    }

    public void setChat(Chat chat) {
        this.chat = chat;
        chat.getMessages().add(this);
    }
}
