package net.skhu.wassup.global.error.response;

import lombok.Builder;
import lombok.Data;
import net.skhu.wassup.global.error.ErrorCode;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorResponse {

    private final int status;

    private final String name;

    private final String code;

    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(e.getHttpStatus().value())
                        .name(e.name())
                        .code(e.getCode())
                        .message(e.getMessage())
                        .build()
                );
    }
}
