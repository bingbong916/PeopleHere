package peoplehere.peoplehere.common.exception;

import lombok.Getter;
import peoplehere.peoplehere.common.response.status.ResponseStatus;

@Getter
public class PlaceException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public PlaceException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public PlaceException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }
}
