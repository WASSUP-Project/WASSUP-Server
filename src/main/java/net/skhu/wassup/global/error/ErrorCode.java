package net.skhu.wassup.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_MATCH_CERTIFICATION_CODE(HttpStatus.BAD_REQUEST, "NM001", "인증번호가 일치하지 않습니다."),
    NOT_MATCH_ACCOUNT_ID_OR_PASSWORD(HttpStatus.BAD_REQUEST, "NM002", "아이디나 비밀번호가 일치하지 않습니다."),

    DUPLICATE_ADMIN_ID(HttpStatus.BAD_REQUEST, "DP001", "이미 존재하는 아이디입니다."),
    DUPLICATE_GROUP_NAME(HttpStatus.BAD_REQUEST, "DP002", "이미 존재하는 그룹 이름입니다."),
    DUPLICATE_GROUP_CODE(HttpStatus.BAD_REQUEST, "DP003", "이미 존재하는 그룹 코드입니다."),

    NOT_FOUND_ADMIN(HttpStatus.NOT_FOUND, "NF001", "존재하지 않는 관리자입니다."),
    NOT_FOUND_GROUP(HttpStatus.NOT_FOUND, "NF002", "그룹 정보가 존재하지 않습니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "NF003", "회원 정보가 존재하지 않습니다."),

    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "IV001", "이메일 형식이 올바르지 않습니다."),

    UNAUTHORIZED_ADMIN(HttpStatus.UNAUTHORIZED, "UU001", "권한이 없습니다.");

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;

}
