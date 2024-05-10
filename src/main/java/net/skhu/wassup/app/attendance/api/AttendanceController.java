package net.skhu.wassup.app.attendance.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.attendance.api.dto.RequestCode;
import net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceMember;
import net.skhu.wassup.app.attendance.api.dto.ResponseCode;
import net.skhu.wassup.app.attendance.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("members")
    @Operation(
            summary = "출석 시 뒷 번호 일치하는 멤버 리스트 조회",
            description = "출석 시 뒷 번호가 같은 멤버 리스트를 조회합니다."
    )
    public ResponseEntity<List<ResponseAttendanceMember>> findMembers(
            @RequestParam String code, @RequestParam String phoneNumber) {
        return ResponseEntity.ok(attendanceService.findMembers(code, phoneNumber));
    }

    @PostMapping("{memberId}")
    @Operation(
            summary = "출석 처리",
            description = "출석을 처리합니다."
    )
    public ResponseEntity<Void> saveAttendance(@RequestBody RequestCode requestCode, @PathVariable Long memberId) {
        String code = requestCode.code();
        attendanceService.saveAttendance(code, memberId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("rate/{groupId}")
    @Operation(
            summary = "출석률 계산",
            description = "출석률을 계산합니다."
    )
    public ResponseEntity<Integer> calculateAttendanceRate(@PathVariable Long groupId) {
        return ResponseEntity.ok(attendanceService.calculateAttendanceRate(groupId));
    }

}
