package peoplehere.peoplehere.common.response.status;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseExceptionResponseStatus implements ResponseStatus {
    // 요청 성공
    SUCCESS(true, 1000, HttpStatus.OK.value(), "요청에 성공하였습니다."),

    // Request 오류
    BAD_REQUEST(false, 2000, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 요청입니다."),
    URL_NOT_FOUND(false, 2001, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 URL 입니다."),
    METHOD_NOT_ALLOWED(false, 2002, HttpStatus.METHOD_NOT_ALLOWED.value(), "해당 URL에서는 지원하지 않는 HTTP Method 입니다."),

    // Server, Database 오류
    SERVER_ERROR(false, 3000, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버에서 오류가 발생하였습니다."),
    DATABASE_ERROR(false, 3001, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스에서 오류가 발생하였습니다."),
    BAD_SQL_GRAMMAR(false, 3002, HttpStatus.INTERNAL_SERVER_ERROR.value(), "SQL에 오류가 있습니다."),

    /**
     * 4000: Authorization 오류
     */
    JWT_ERROR(false, 4000, HttpStatus.UNAUTHORIZED.value(), "JWT에서 오류가 발생하였습니다."),
    TOKEN_NOT_FOUND(false, 4001, HttpStatus.BAD_REQUEST.value(), "토큰이 HTTP Header에 없습니다."),
    UNSUPPORTED_TOKEN_TYPE(false, 4002, HttpStatus.BAD_REQUEST.value(), "지원되지 않는 토큰 형식입니다."),
    INVALID_TOKEN(false, 4003, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰입니다."),
    MALFORMED_TOKEN(false, 4004, HttpStatus.UNAUTHORIZED.value(), "토큰이 올바르게 구성되지 않았습니다."),
    EXPIRED_TOKEN(false, 4005, HttpStatus.UNAUTHORIZED.value(), "만료된 토큰입니다."),
    TOKEN_MISMATCH(false, 4006, HttpStatus.UNAUTHORIZED.value(), "로그인 정보가 토큰 정보와 일치하지 않습니다."),

    // User 오류
    INVALID_USER_VALUE(false, 5000, HttpStatus.BAD_REQUEST.value(), "회원가입 요청에서 잘못된 값이 존재합니다."),
    DUPLICATE_EMAIL(false, 5001, HttpStatus.BAD_REQUEST.value(), "이미 존재하는 이메일입니다."),
    DUPLICATE_PHONE_NUMBER(false, 5002, HttpStatus.BAD_REQUEST.value(), "이미 존재하는 전화번호입니다."),
    USER_NOT_FOUND(false, 5003, HttpStatus.BAD_REQUEST.value(), "존재하지 않는 회원입니다."),
    PASSWORD_NO_MATCH(false, 5004, HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다."),
    INVALID_USER_STATUS(false, 5005, HttpStatus.BAD_REQUEST.value(), "잘못된 회원 status 값입니다."),
    USER_NOT_ADULT(false, 5006, HttpStatus.BAD_REQUEST.value(), "만 18세 이상이 아닙니다."),
    USER_DELETED(false, 5007, HttpStatus.BAD_REQUEST.value(), "삭제된 사용자입니다."),
    USER_NOT_LOGGED_IN(false, 5008, HttpStatus.UNAUTHORIZED.value(), "로그인하지 않은 사용자입니다."),
    SAME_AS_OLD_PASSWORD(false, 5009, HttpStatus.BAD_REQUEST.value(), "새 비밀번호는 기존 비밀번호와 달라야 합니다."),

    // Tour 오류
    TOUR_NOT_FOUND(false, 6000, HttpStatus.NOT_FOUND.value(), "존재하지 않는 투어입니다."),
    INVALID_TOUR_VALUE(false, 6001, HttpStatus.BAD_REQUEST.value(), "투어 요청에서 잘못된 값이 존재합니다."),
    DUPLICATE_TOUR_NAME(false, 6002, HttpStatus.BAD_REQUEST.value(), "이미 존재하는 투어 이름입니다."),
    TOUR_INACTIVATED(false, 6003, HttpStatus.BAD_REQUEST.value(), "비활성화된 투어입니다."),
    TOUR_ALREADY_JOINED(false, 6004, HttpStatus.BAD_REQUEST.value(), "이미 참여한 투어입니다."),

    // TourDate 오류
    TOUR_DATE_NOT_FOUND(false, 6005, HttpStatus.NOT_FOUND.value(), "존재하지 않는 일정입니다."),
    TOUR_DATE_IN_PAST(false, 6006, HttpStatus.BAD_REQUEST.value(), "과거의 날짜입니다."),
    SAME_AS_TOUR_LEADER(false, 6007, HttpStatus.BAD_REQUEST.value(), "투어 리더는 본인의 투어에 참여할 수 없습니다."),
    TOUR_HISTORY_NOT_FOUND(false, 6008, HttpStatus.NOT_FOUND.value(), "존재하지 않는 참여 정보입니다."),
    INVALID_TOUR_HISTORY_STATUS(false, 6009, HttpStatus.BAD_REQUEST.value(), "잘못된 참여 정보 status 값입니다."),

    // Place 오류
    PLACE_NOT_FOUND(false, 7000, HttpStatus.NOT_FOUND.value(), "존재하지 않는 장소입니다."),
    INVALID_PLACE_VALUE(false, 7001, HttpStatus.BAD_REQUEST.value(), "장소 요청에서 잘못된 값이 존재합니다."),
    DUPLICATE_PLACE_NAME(false, 7002, HttpStatus.BAD_REQUEST.value(), "이미 존재하는 장소 이름입니다."),

    // Review 오류
    REVIEW_NOT_FOUND(false, 8000, HttpStatus.NOT_FOUND.value(), "리뷰를 찾을 수 없습니다."),
    INVALID_REVIEW_VALUE(false, 8001, HttpStatus.BAD_REQUEST.value(), "리뷰 요청 데이터가 유효하지 않습니다."),
    DUPLICATE_REVIEW(false, 8002, HttpStatus.BAD_REQUEST.value(), "이미 존재하는 리뷰입니다."),
    SAME_AS_REVIEWEE_USER(false, 8003, HttpStatus.BAD_REQUEST.value(), "본인에게 리뷰를 작성할 수 없습니다."),

    // Message 오류
    MESSAGE_NOT_FOUND(false, 9000, HttpStatus.NOT_FOUND.value(), "메시지를 찾을 수 없습니다."),
    INVALID_MESSAGE_VALUE(false, 9001, HttpStatus.BAD_REQUEST.value(), "메시지 요청 데이터가 유효하지 않습니다."),
    CHAT_NOT_FOUND_FOR_MESSAGE(false, 9002, HttpStatus.BAD_REQUEST.value(), "메시지에 해당하는 채팅방을 찾을 수 없습니다."),

    // Chat 오류
    CHAT_NOT_FOUND(false, 10000, HttpStatus.NOT_FOUND.value(), "채팅방을 찾을 수 없습니다."),
    INVALID_CHAT_VALUE(false, 10001, HttpStatus.BAD_REQUEST.value(), "채팅방 요청 데이터가 유효하지 않습니다."),
    USER_NOT_IN_CHAT(false, 10002, HttpStatus.BAD_REQUEST.value(), "사용자가 채팅방에 존재하지 않습니다."),

    // JWT 오류
    JWT_INVALID_TOKEN(false, 11000, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 JWT 토큰입니다."),
    JWT_TOKEN_EXPIRED(false, 11001, HttpStatus.UNAUTHORIZED.value(), "JWT 토큰이 만료되었습니다."),
    JWT_TOKEN_UNSUPPORTED(false, 11002, HttpStatus.BAD_REQUEST.value(), "지원하지 않는 JWT 토큰입니다."),
    JWT_TOKEN_MALFORMED(false, 11003, HttpStatus.BAD_REQUEST.value(), "잘못된 형식의 JWT 토큰입니다."),
    JWT_SIGNATURE_INVALID(false, 11004, HttpStatus.BAD_REQUEST.value(), "JWT 서명이 유효하지 않습니다."),
    JWT_TOKEN_NOT_PROVIDED(false, 11005, HttpStatus.UNAUTHORIZED.value(), "JWT 토큰이 제공되지 않았습니다."),

    // Language 오류
    LANGUAGE_NOT_FOUND(false, 12000, HttpStatus.NOT_FOUND.value(), "언어를 찾을 수 없습니다."),

    // Question 오류
    QUESTION_NOT_FOUND(false, 13000, HttpStatus.NOT_FOUND.value(), "질문을 찾을 수 없습니다.");



    private final boolean success;
    private final int code;
    private final int status;
    private final String message;

    BaseExceptionResponseStatus(boolean success, int code, int status, String message) {
        this.success = success;
        this.code = code;
        this.status = status;
        this.message = message;
    }

    @Override
    public boolean getSuccess() {
        return success;
    }
}
