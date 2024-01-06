package peoplehere.peoplehere.common.exception.jwt.bad_request;

import peoplehere.peoplehere.common.exception.jwt.bad_request.JwtBadRequestException;
import peoplehere.peoplehere.common.response.status.ResponseStatus;

public class JwtUnsupportedTokenException extends JwtBadRequestException {

    public JwtUnsupportedTokenException(ResponseStatus exceptionStatus) {
        super(exceptionStatus);
    }
}
