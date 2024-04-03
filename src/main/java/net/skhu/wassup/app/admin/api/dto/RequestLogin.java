package net.skhu.wassup.app.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestLogin(

        @NotBlank(message = "아이디를 입력해주세요.")
        @Size(min = 5, max = 20)
        String adminId,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 20)
        String password) {

}
