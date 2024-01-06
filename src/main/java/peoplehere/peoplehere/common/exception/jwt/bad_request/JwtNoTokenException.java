package peoplehere.peoplehere.common.exception.jwt.bad_request;

import peoplehere.peoplehere.common.exception.jwt.bad_request.JwtBadRequestException;
import peoplehere.peoplehere.common.response.status.ResponseStatus;

public class JwtNoTokenException extends JwtBadRequestException {

    public JwtNoTokenException(ResponseStatus exceptionStatus) {
        super(exceptionStatus);
    }
}
