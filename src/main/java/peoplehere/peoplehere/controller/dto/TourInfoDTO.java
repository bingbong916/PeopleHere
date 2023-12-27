package peoplehere.peoplehere.controller.dto;

import lombok.Getter;
import peoplehere.peoplehere.domain.User;

import java.sql.Date;

@Getter
public class TourInfoDTO {

    private String name;
    private int budget;
    private Date startDate;
    private int time;
    private String imageUrl;
    private String content;
    private User user;

    public TourInfoDTO(String name, int budget, Date startDate, int time, String imageUrl, String content, User user) {
        this.name = name;
        this.budget = budget;
        this.startDate = startDate;
        this.time = time;
        this.imageUrl = imageUrl;
        this.content = content;
        this.user = user;
    }
}
