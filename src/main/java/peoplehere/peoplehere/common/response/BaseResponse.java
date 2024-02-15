package peoplehere.peoplehere.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import peoplehere.peoplehere.common.response.status.ResponseStatus;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.SUCCESS;

@Getter
@JsonPropertyOrder({"success", "code", "status", "message", "result"})
public class BaseResponse<T> {

    private final boolean success;
    private final int code;
    private final int status;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    public BaseResponse(T result) {
        this.success = true;
        this.code = SUCCESS.getCode();
        this.status = SUCCESS.getStatus();
        this.message = SUCCESS.getMessage();
        this.result = result;
    }

    public BaseResponse(ResponseStatus status, T result) {
        this.success = status.getSuccess();
        this.code = status.getCode();
        this.status = status.getStatus();
        this.message = status.getMessage();
        this.result = result;
    }
}
