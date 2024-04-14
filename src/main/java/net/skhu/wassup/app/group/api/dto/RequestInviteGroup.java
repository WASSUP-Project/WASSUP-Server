package net.skhu.wassup.app.group.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "그룹 초대 요청")
public record RequestInviteGroup(

        @Schema(description = "전송할 번호")
        @NotBlank(message = "전송할 전화번호를 입력해주세요.")
        String phoneNumber,

        @Schema(description = "그룹 초대 링크")
        @NotBlank(message = "그룹 초대 링크를 입력해주세요.")
        String link) {

}
