package net.skhu.wassup.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.skhu.wassup.global.error.ErrorCode;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    ErrorCode errorCode;
}
