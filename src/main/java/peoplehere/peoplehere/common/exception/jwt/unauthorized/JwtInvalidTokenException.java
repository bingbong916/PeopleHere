package peoplehere.peoplehere.common.exception.jwt.unauthorized;

import peoplehere.peoplehere.common.response.status.ResponseStatus;

public class JwtInvalidTokenException extends JwtUnauthorizedTokenException {

    public JwtInvalidTokenException(ResponseStatus exceptionStatus) {
        super(exceptionStatus);
    }
}
