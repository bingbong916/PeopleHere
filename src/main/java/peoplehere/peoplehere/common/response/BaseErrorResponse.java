package peoplehere.peoplehere.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import peoplehere.peoplehere.common.response.status.ResponseStatus;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({"success", "code", "status", "message", "timestamp"})
public class BaseErrorResponse {

    private final boolean success;
    private final int code;
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;

    public BaseErrorResponse(ResponseStatus status) {
        this.success = status.getSuccess();
        this.code = status.getCode();
        this.status = status.getStatus();
        this.message = status.getMessage();
        this.timestamp = LocalDateTime.now();
    }

    public BaseErrorResponse(boolean success, ResponseStatus status, String customMessage) {
        this.success = success;
        this.code = status.getCode();
        this.status = status.getStatus();
        this.message = customMessage;
        this.timestamp = LocalDateTime.now();
    }
}
