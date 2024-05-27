package net.skhu.wassup.app.attendance.service;

import java.util.List;
import net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceInfo;
import net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceMember;
import net.skhu.wassup.app.attendance.api.dto.ResponseCode;

public interface AttendanceService {

    ResponseCode generateAttendanceCode(Long groupId);

    List<ResponseAttendanceMember> findMembers(String code, String phoneNumber);

    void saveAttendance(String code, Long memberId);

    void saveLeaving(String code, Long memberId);

    ResponseAttendanceInfo getAttendanceInfo(Long groupId);

}
