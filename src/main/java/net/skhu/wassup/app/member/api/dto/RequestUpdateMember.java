package net.skhu.wassup.app.member.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "멤버 수정 요청")
public record RequestUpdateMember(

        @Schema(description = "이름")
        String name,

        @Schema(description = "전화번호")
        String phoneNumber,

        @Schema(description = "생년월일")
        String birth,

        @Schema(description = "특이사항")
        String specifics) {

}
