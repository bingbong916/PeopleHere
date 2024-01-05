package peoplehere.peoplehere.common.exception.jwt.unauthorized;

import peoplehere.peoplehere.common.response.status.ResponseStatus;

public class JwtMalformedTokenException extends JwtUnauthorizedTokenException {

    public JwtMalformedTokenException(ResponseStatus exceptionStatus) {
        super(exceptionStatus);
    }
}
