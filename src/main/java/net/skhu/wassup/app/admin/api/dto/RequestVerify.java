package net.skhu.wassup.app.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestVerify(

        @NotBlank(message = "전화번호를 입력해주세요.")
        @Pattern(regexp = "010[0-9]{8}")
        String phoneNumber,

        @NotBlank(message = "인증 코드를 입력해주세요.")
        @Size(min = 6, max = 6, message = "인증 번호는 6자리로 입력해주세요.")
        String inputCertificationCode) {

}
