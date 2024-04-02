package net.skhu.wassup.app.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RequestVerify(

        @NotBlank(message = "전화번호를 입력해주세요.")
        @Pattern(regexp = "010[0-9]{8}")
        String phoneNumber,

        @NotBlank(message = "인증 코드를 입력해주세요.")
        String inputCertificationCode) {

}
