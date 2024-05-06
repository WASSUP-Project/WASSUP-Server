package net.skhu.wassup.app.attendance.service;

import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.attendance.api.dto.ResponseCode;
import net.skhu.wassup.app.certification.AttendanceCodeService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceCodeService attendanceCodeService;

    @Override
    public ResponseCode generateAttendanceCode(Long groupId) {
        return ResponseCode.builder()
                .code(attendanceCodeService.createCode(groupId))
                .build();
    }

    @Override
    public Long test(String code) {
        return attendanceCodeService.findGroupIdByCode(code);
    }
}
