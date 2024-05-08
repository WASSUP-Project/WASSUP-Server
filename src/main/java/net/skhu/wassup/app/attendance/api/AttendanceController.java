package net.skhu.wassup.app.attendance.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.attendance.api.dto.ResponseCode;
import net.skhu.wassup.app.attendance.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/attendances")
@Tag(name = "Attendance", description = "출석 관련 API")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("code/{groupId}")
    @Operation(
            summary = "출석 코드 생성",
            description = "출석 코드를 생성합니다."
    )
    public ResponseEntity<ResponseCode> generateAttendanceCode(@PathVariable Long groupId) {
        return ResponseEntity.ok(attendanceService.generateAttendanceCode(groupId));
    }

    @GetMapping("test/{code}")
    public String test(@PathVariable String code) {
        return attendanceService.test(code).toString();
    }

}
