package peoplehere.peoplehere.common.exception;

import peoplehere.peoplehere.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class TourException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public TourException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public TourException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }
}
