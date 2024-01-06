package peoplehere.peoplehere.common.exception.jwt.unauthorized;

import peoplehere.peoplehere.common.response.status.ResponseStatus;

public class JwtExpiredTokenException extends JwtUnauthorizedTokenException {

    public JwtExpiredTokenException(ResponseStatus exceptionStatus) {
        super(exceptionStatus);
    }
}
