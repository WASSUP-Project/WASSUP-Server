package net.skhu.wassup.app.group.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "이메일 인증 요청")
public record RequestVerify(

        @Schema(description = "이메일")
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email
        String email,

        @Schema(description = "인증 코드")
        @NotBlank(message = "인증 코드를 입력해주세요.")
        String inputCertificationCode) {

}
