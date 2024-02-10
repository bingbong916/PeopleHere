package peoplehere.peoplehere.controller.dto.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostImageRequest {

    private String encodingString;
    private String originalFileName;

}
