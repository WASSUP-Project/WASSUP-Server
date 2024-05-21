package net.skhu.wassup.app.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "비밀번호 찾기 요청")
public record RequestFindPassword(

        @Schema(description = "아이디")
        String adminId,

        @Schema(description = "전화번호")
        String phoneNumber,

        @Schema(description = "인증번호")
        String certificationCode,

        @Schema(description = "새로운 비밀번호")
        String newPassword) {

}
