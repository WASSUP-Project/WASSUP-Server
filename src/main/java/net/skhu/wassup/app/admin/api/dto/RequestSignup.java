package net.skhu.wassup.app.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "회원가입 요청")
public record RequestSignup(

        @Schema(description = "아이디를 5자 이상 20자 이하로 입력해주세요.")
        @NotBlank(message = "아이디를 입력해주세요.")
        @Size(min = 5, max = 20)
        String adminId,

        @Schema(description = "비밀번호를 8자 이상 20자 이하로 입력해주세요.")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 20)
        String password,

        @Schema(description = "이름")
        @NotBlank(message = "이름을 입력해주세요.")
        String name,

        @Schema(description = "전화번호", example = "01012345678")
        @NotBlank(message = "전화번호를 입력해주세요.")
        @Pattern(regexp = "010[0-9]{8}", message = "전화번호는 형식에 맞게 11자리 숫자로 입력해주세요.")
        String phoneNumber) {

}