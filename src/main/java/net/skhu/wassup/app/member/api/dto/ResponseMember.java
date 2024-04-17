package net.skhu.wassup.app.member.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "멤버 정보 조회")
public record ResponseMember(

        @Schema(description = "멤버 ID")
        Long id,

        @Schema(description = "이름")
        String name,

        @Schema(description = "주소")
        String address,

        @Schema(description = "전화번호")
        String phoneNumber,

        @Schema(description = "특이사항")
        String specifics) {

}