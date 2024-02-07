package peoplehere.peoplehere.controller.dto.place;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPlaceRequest { // 구글맵 API 특성상 제약조건 불필요
    private String content; //구글 기본 장소 설명..? 필요 없기는 할듯?
    private List<MultipartFile> images;
    private String address;
    private int order;
}