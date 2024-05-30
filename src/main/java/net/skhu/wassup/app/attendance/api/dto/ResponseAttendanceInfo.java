package net.skhu.wassup.app.attendance.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "출석 정보 조회")
public record ResponseAttendanceInfo(

        @Schema(description = "출석률")
        int attendanceRate,

        @Schema(description = "미출석 멤버 조회")
        List<ResponseAttendanceMember> notAttendanceMembers) {

}
