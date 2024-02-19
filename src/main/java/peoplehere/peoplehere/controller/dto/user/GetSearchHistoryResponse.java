package peoplehere.peoplehere.controller.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetSearchHistoryResponse {
    private String placeKey;
    private String placeName;
    private String placeAddress;
}
