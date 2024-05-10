package net.skhu.wassup.app.attendance.api.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record ResponseAttendanceInfo(
        int attendanceRate,
        List<ResponseAttendanceMember> notAttendanceMembers
) {
}
