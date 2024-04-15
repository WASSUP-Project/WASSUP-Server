package net.skhu.wassup.app.member.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "그룹 가입 요청")
public record RequestMember(

        @Schema(description = "이름")
        @NotBlank(message = "이름을 입력해주세요.")
        String name,

        @Schema(description = "주소")
        @NotBlank(message = "주소를 입력해주세요.")
        String address,

        @Schema(description = "전화번호")
        @NotBlank(message = "전화번호를 입력해주세요.")
        String phoneNumber,

        @Schema(description = "특이사항")
        String specifics,

        @Schema(description = "그룹 코드")
        @NotBlank(message = "그룹 코드를 입력해주세요.")
        String groupCode) {

}
