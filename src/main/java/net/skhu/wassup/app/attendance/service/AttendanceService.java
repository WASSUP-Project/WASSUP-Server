package net.skhu.wassup.app.attendance.service;

import java.util.List;
import net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceGroupMember;
import net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceInfo;
import net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceMember;
import net.skhu.wassup.app.attendance.api.dto.ResponseCode;
import net.skhu.wassup.app.attendance.domain.Status;

public interface AttendanceService {

    ResponseCode generateAttendanceCode(Long groupId);

    List<ResponseAttendanceMember> findMembers(String code, String phoneNumber);

    List<ResponseAttendanceMember> findMembersForLeaving(String code, String phoneNumber);

    void saveAttendance(String code, Long memberId);

    void saveLeaving(String code, Long memberId);

    ResponseAttendanceInfo getAttendanceInfo(Long groupId);

    List<ResponseAttendanceGroupMember> getAttendanceMembers(Long groupId);

    void updateAttendanceStatus(Long memberId, Status status);

    void updateAllAttendanceStatus(String code, Status status);

}
