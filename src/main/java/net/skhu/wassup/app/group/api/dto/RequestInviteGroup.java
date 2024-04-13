package net.skhu.wassup.app.group.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "그룹 초대 요청")
public record RequestInviteGroup(

        @Schema(description = "전송할 번호")
        String phoneNumber,

        @Schema(description = "그룹 초대 링크")
        String link) {

}
