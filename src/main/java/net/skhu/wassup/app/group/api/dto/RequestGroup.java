package net.skhu.wassup.app.group.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "그룹 생성 요청")
public record RequestGroup(

        @Schema(description = "그룹 이름")
        @NotBlank(message = "그룹 이름을 입력해주세요.")
        String groupName,

        @Schema(description = "그룹 설명")
        @NotBlank(message = "그룹을 설명해주세요.")
        String groupDescription,

        @Schema(description = "그룹 주소")
        @NotBlank(message = "그룹 주소를 입력해주세요.")
        String address,

        @Schema(description = "사업자 번호")
        String businessNumber,

        @Schema(description = "사업자 이메일")
        @NotBlank(message = "사업자 이메일을 입력해주세요.")
        @Email
        String email,

        @Schema(description = "그룹 이미지 URL")
        @NotBlank(message = "그룹 이미지 URL을 입력해주세요.")
        String imageUrl) {

}
