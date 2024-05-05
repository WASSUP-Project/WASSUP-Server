package net.skhu.wassup.app.attendance.service;

import net.skhu.wassup.app.attendance.api.dto.ResponseCode;

public interface AttendanceService {

    ResponseCode generateAttendanceCode(Long groupId);

    Long test(String code);

}
