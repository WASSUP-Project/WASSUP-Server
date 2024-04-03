package net.skhu.wassup.app.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestSignup(

        @NotBlank(message = "아이디를 입력해주세요.")
        @Size(min = 5, max = 20, message = "아이디는 5자 이상 20자 이하로 입력해주세요.")
        String adminId,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
        String password,

        @NotBlank(message = "이름을 입력해주세요.")
        String name,

        @NotBlank(message = "전화번호를 입력해주세요.")
        @Pattern(regexp = "010[0-9]{8}", message = "전화번호는 형식에 맞게 11자리 숫자로 입력해주세요.")
        String phoneNumber) {

}