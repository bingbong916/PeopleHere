package peoplehere.peoplehere.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetOnboardingStatusResponse {
    private boolean hasCreatedTour;
    private String message;
}
