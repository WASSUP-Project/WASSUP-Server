package net.skhu.wassup.app.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "로그인 요청")
public record RequestLogin(

        @Schema(description = "아이디")
        @Size(min = 5, max = 20)
        @NotBlank(message = "아이디를 입력해주세요.")
        String adminId,

        @Schema(description = "비밀번호")
        @Size(min = 8, max = 20)
        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password) {

}
