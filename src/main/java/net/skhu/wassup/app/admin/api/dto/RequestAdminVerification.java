package net.skhu.wassup.app.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "관리자 아이디와 전화번호 일치 검증")
public record RequestAdminVerification(

        @Schema(description = "관리자 아이디")
        String adminId,

        @Schema(description = "전화번호")
        String phoneNumber) {

}
