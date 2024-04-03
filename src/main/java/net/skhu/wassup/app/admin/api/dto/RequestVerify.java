package net.skhu.wassup.app.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "인증 요청")
public record RequestVerify(

        @Schema(description = "전화번호", example = "01012345678")
        @NotBlank(message = "전화번호를 입력해주세요.")
        @Pattern(regexp = "010[0-9]{8}")
        String phoneNumber,

        @Schema(description = "인증 코드")
        @NotBlank(message = "인증 코드를 입력해주세요.")
        @Size(min = 6, max = 6, message = "인증 번호는 6자리로 입력해주세요.")
        String inputCertificationCode) {

}
