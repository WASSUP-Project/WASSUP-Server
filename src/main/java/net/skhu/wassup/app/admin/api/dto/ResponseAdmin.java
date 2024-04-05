package net.skhu.wassup.app.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "관리자 조회")
public record ResponseAdmin(

        @Schema(description = "관리자 ID")
        Long id,

        @Schema(description = "관리자 이름")
        String name,

        @Schema(description = "전화번호")
        String phoneNumber) {

}
