package peoplehere.peoplehere.controller.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPhoneNumberCheckResponse {
    private boolean isPhoneNumberAvailable;
    private String message;
}