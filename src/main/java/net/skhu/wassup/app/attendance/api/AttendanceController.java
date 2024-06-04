package net.skhu.wassup.app.attendance.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.attendance.api.dto.RequestCode;
import net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceGroupMember;
import net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceInfo;
import net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceMember;
import net.skhu.wassup.app.attendance.api.dto.ResponseCode;
import net.skhu.wassup.app.attendance.domain.Status;
import net.skhu.wassup.app.attendance.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/attendances")
@Tag(name = "Attendance Controller", description = "출석 관련 API")
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
            summary = "등원 시 뒷 번호 일치하는 멤버 리스트 조회",
            description = "등원 시 뒷 번호가 같은 멤버 리스트를 조회합니다."
    )
    @Parameter(name = "code", description = "출석 코드")
    @Parameter(name = "phoneNumber", description = "전화번호 뒷자리")
    public ResponseEntity<List<ResponseAttendanceMember>> findMembers(
            @RequestParam String code, @RequestParam String phoneNumber) {
        return ResponseEntity.ok(attendanceService.findMembers(code, phoneNumber));
    }

    @GetMapping("leaving/members")
    @Operation(
            summary = "하원 시 뒷 번호 일치하는 멤버 리스트 조회",
            description = "하원 시 뒷 번호가 같은 멤버 리스트를 조회합니다."
    )
    @Parameter(name = "code", description = "출석 코드")
    @Parameter(name = "phoneNumber", description = "전화번호 뒷자리")
    public ResponseEntity<List<ResponseAttendanceMember>> findLeavingMembers(
            @RequestParam String code, @RequestParam String phoneNumber) {
        return ResponseEntity.ok(attendanceService.findMembersForLeaving(code, phoneNumber));
    }

    @PostMapping("{memberId}")
    @Operation(
            summary = "등원 처리",
            description = "등원을 처리합니다."
    )
    public ResponseEntity<Void> saveAttendance(@RequestBody RequestCode requestCode, @PathVariable Long memberId) {
        String code = requestCode.code();
        attendanceService.saveAttendance(code, memberId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("leaving/{memberId}")
    @Operation(
            summary = "하원 처리",
            description = "하원을 처리합니다."
    )
    public ResponseEntity<Void> saveLeaving(@RequestBody RequestCode requestCode, @PathVariable Long memberId) {
        String code = requestCode.code();
        attendanceService.saveLeaving(code, memberId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("info/{groupId}")
    @Operation(
            summary = "출석률과 미출석 멤버 조회",
            description = "금일 출석률과 미출석 멤버를 조회합니다."
    )
    public ResponseEntity<ResponseAttendanceInfo> getAttendanceInfo(@PathVariable Long groupId) {
        return ResponseEntity.ok(attendanceService.getAttendanceInfo(groupId));
    }

    @GetMapping("members/{groupId}")
    @Operation(
            summary = "그룹 멤버 출석 상태 조회",
            description = "그룹 멤버의 출석 상태를 조회합니다."
    )
    public ResponseEntity<List<ResponseAttendanceGroupMember>> getAttendanceMembers(@PathVariable Long groupId) {
        return ResponseEntity.ok(attendanceService.getAttendanceMembers(groupId));
    }

    @PutMapping("members/{memberId}")
    @Operation(
            summary = "출석 상태 변경",
            description = "멤버의 출석 상태를 변경합니다."
    )
    public ResponseEntity<Void> updateAttendanceStatus(@PathVariable Long memberId, @RequestParam Status status) {
        attendanceService.updateAttendanceStatus(memberId, status);

        return ResponseEntity.ok().build();
    }
}
