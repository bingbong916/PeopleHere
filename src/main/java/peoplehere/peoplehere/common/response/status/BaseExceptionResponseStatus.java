package peoplehere.peoplehere.common.response.status;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseExceptionResponseStatus implements ResponseStatus {
    // 요청 성공
    SUCCESS(1000, HttpStatus.OK.value(), "요청에 성공하였습니다."),

    // Request 오류
    BAD_REQUEST(2000, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 요청입니다."),
    URL_NOT_FOUND(2001, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 URL 입니다."),
    METHOD_NOT_ALLOWED(2002, HttpStatus.METHOD_NOT_ALLOWED.value(), "해당 URL에서는 지원하지 않는 HTTP Method 입니다."),

    // Server, Database 오류
    SERVER_ERROR(3000, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버에서 오류가 발생하였습니다."),
    DATABASE_ERROR(3001, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스에서 오류가 발생하였습니다."),
    BAD_SQL_GRAMMAR(3002, HttpStatus.INTERNAL_SERVER_ERROR.value(), "SQL에 오류가 있습니다."),

    /**
     * 4000: Authorization 오류
     */
    JWT_ERROR(4000, HttpStatus.UNAUTHORIZED.value(), "JWT에서 오류가 발생하였습니다."),
    TOKEN_NOT_FOUND(4001, HttpStatus.BAD_REQUEST.value(), "토큰이 HTTP Header에 없습니다."),
    UNSUPPORTED_TOKEN_TYPE(4002, HttpStatus.BAD_REQUEST.value(), "지원되지 않는 토큰 형식입니다."),
    INVALID_TOKEN(4003, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰입니다."),
    MALFORMED_TOKEN(4004, HttpStatus.UNAUTHORIZED.value(), "토큰이 올바르게 구성되지 않았습니다."),
    EXPIRED_TOKEN(4005, HttpStatus.UNAUTHORIZED.value(), "만료된 토큰입니다."),
    TOKEN_MISMATCH(4006, HttpStatus.UNAUTHORIZED.value(), "로그인 정보가 토큰 정보와 일치하지 않습니다."),

    // User 오류
    INVALID_USER_VALUE(5000, HttpStatus.BAD_REQUEST.value(), "회원가입 요청에서 잘못된 값이 존재합니다."),
    DUPLICATE_EMAIL(5001, HttpStatus.BAD_REQUEST.value(), "이미 존재하는 이메일입니다."),
    USER_NOT_FOUND(5002, HttpStatus.BAD_REQUEST.value(), "존재하지 않는 회원입니다."),
    PASSWORD_NO_MATCH(5003, HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다."),
    INVALID_USER_STATUS(5004, HttpStatus.BAD_REQUEST.value(), "잘못된 회원 status 값입니다."),
    EMAIL_NOT_FOUND(5005, HttpStatus.BAD_REQUEST.value(), "존재하지 않는 이메일입니다."),
    USER_DELETED(5006, HttpStatus.BAD_REQUEST.value(), "삭제된 사용자입니다."),

    // Tour 오류
    TOUR_NOT_FOUND(6000, HttpStatus.NOT_FOUND.value(), "존재하지 않는 투어입니다."),
    INVALID_TOUR_VALUE(6001, HttpStatus.BAD_REQUEST.value(), "투어 요청에서 잘못된 값이 존재합니다."),
    DUPLICATE_TOUR_NAME(6002, HttpStatus.BAD_REQUEST.value(), "이미 존재하는 투어 이름입니다."),
    TOUR_INACTIVATED(6003, HttpStatus.BAD_REQUEST.value(), "비활성화된 투어입니다."),
    TOUR_ALREADY_JOINED(6004, HttpStatus.BAD_REQUEST.value(), "이미 참여한 투어입니다."),
    TOUR_DATE_NOT_FOUND(6005, HttpStatus.BAD_REQUEST.value(), "존재하지 않는 일정입니다."),
    TOUR_DATE_IN_PAST(6006, HttpStatus.BAD_REQUEST.value(), "과거의 날짜입니다."),
    DUPLICATE_TOUR_DATE(6004, HttpStatus.BAD_REQUEST.value(), "중복된 일정입니다."),


    // Place 오류
    PLACE_NOT_FOUND(7000, HttpStatus.NOT_FOUND.value(), "존재하지 않는 장소입니다."),
    INVALID_PLACE_VALUE(7001, HttpStatus.BAD_REQUEST.value(), "장소 요청에서 잘못된 값이 존재합니다."),
    DUPLICATE_PLACE_NAME(7002, HttpStatus.BAD_REQUEST.value(), "이미 존재하는 장소 이름입니다."),

    // Review 오류
    REVIEW_NOT_FOUND(8000, HttpStatus.NOT_FOUND.value(), "리뷰를 찾을 수 없습니다."),
    INVALID_REVIEW_VALUE(8001, HttpStatus.BAD_REQUEST.value(), "리뷰 요청 데이터가 유효하지 않습니다."),
    DUPLICATE_REVIEW(8002, HttpStatus.BAD_REQUEST.value(), "이미 존재하는 리뷰입니다."),

    // Message 오류
    MESSAGE_NOT_FOUND(9000, HttpStatus.NOT_FOUND.value(), "메시지를 찾을 수 없습니다."),
    INVALID_MESSAGE_VALUE(9001, HttpStatus.BAD_REQUEST.value(), "메시지 요청 데이터가 유효하지 않습니다."),
    CHAT_NOT_FOUND_FOR_MESSAGE(9002, HttpStatus.BAD_REQUEST.value(), "메시지에 해당하는 채팅방을 찾을 수 없습니다."),

    // Chat 오류
    CHAT_NOT_FOUND(10000, HttpStatus.NOT_FOUND.value(), "채팅방을 찾을 수 없습니다."),
    INVALID_CHAT_VALUE(10001, HttpStatus.BAD_REQUEST.value(), "채팅방 요청 데이터가 유효하지 않습니다."),
    USER_NOT_IN_CHAT(10002, HttpStatus.BAD_REQUEST.value(), "사용자가 채팅방에 존재하지 않습니다."),

    // JWT 오류
    JWT_INVALID_TOKEN(11000, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 JWT 토큰입니다."),
    JWT_TOKEN_EXPIRED(11001, HttpStatus.UNAUTHORIZED.value(), "JWT 토큰이 만료되었습니다."),
    JWT_TOKEN_UNSUPPORTED(11002, HttpStatus.BAD_REQUEST.value(), "지원하지 않는 JWT 토큰입니다."),
    JWT_TOKEN_MALFORMED(11003, HttpStatus.BAD_REQUEST.value(), "잘못된 형식의 JWT 토큰입니다."),
    JWT_SIGNATURE_INVALID(11004, HttpStatus.BAD_REQUEST.value(), "JWT 서명이 유효하지 않습니다."),
    JWT_TOKEN_NOT_PROVIDED(11005, HttpStatus.UNAUTHORIZED.value(), "JWT 토큰이 제공되지 않았습니다.");


    private final int code;
    private final int status;
    private final String message;

    BaseExceptionResponseStatus(int code, int status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
