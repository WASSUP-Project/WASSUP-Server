package net.skhu.wassup.app.attendance.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "출석 멤버 조회")
public record ResponseAttendanceMember(

        @Schema(description = "멤버 ID")
        Long memberId,

        @Schema(description = "멤버 이름")
        String memberName) {

}
