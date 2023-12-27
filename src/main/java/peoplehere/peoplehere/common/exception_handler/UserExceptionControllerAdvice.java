package peoplehere.peoplehere.common.exception_handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import peoplehere.peoplehere.common.exception.UserException;
import peoplehere.peoplehere.common.response.BaseErrorResponse;

@Slf4j
@RestControllerAdvice
public class UserExceptionControllerAdvice {

    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseErrorResponse handleUserException(UserException e) {
        log.error("UserException: {}", e.getMessage(), e);
        return new BaseErrorResponse(e.getExceptionStatus());
    }
}
