package net.skhu.wassup.app.attendance.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import net.skhu.wassup.app.attendance.domain.Status;

@Builder
@Schema(description = "그룹 멤버 출석 상태 조회")
public record ResponseAttendanceGroupMember(

        @Schema(description = "멤버 ID")
        Long memberId,

        @Schema(description = "멤버 이름")
        String memberName,

        @Schema(description = "출석 상태")
        Status status) {

}
