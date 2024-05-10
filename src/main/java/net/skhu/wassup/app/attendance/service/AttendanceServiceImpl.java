package net.skhu.wassup.app.attendance.service;

import static net.skhu.wassup.app.attendance.domain.Status.ATTENDANCE;
import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_GROUP;
import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_MEMBER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.attendance.api.dto.RequestAttendance;
import net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceMember;
import net.skhu.wassup.app.attendance.api.dto.ResponseCode;
import net.skhu.wassup.app.attendance.domain.Attendance;
import net.skhu.wassup.app.attendance.domain.AttendanceRepository;
import net.skhu.wassup.app.certification.AttendanceCodeService;
import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.group.domain.GroupRepository;
import net.skhu.wassup.app.member.domain.Member;
import net.skhu.wassup.app.member.domain.MemberRepository;
import net.skhu.wassup.global.error.exception.CustomException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final static int ATTENDANCE_RATE = 100;

    private final AttendanceCodeService attendanceCodeService;

    private final AttendanceRepository attendanceRepository;

    private final MemberRepository memberRepository;

    private final GroupRepository groupRepository;

    @Override
    public ResponseCode generateAttendanceCode(Long groupId) {
        return ResponseCode.builder()
                .code(attendanceCodeService.createCode(groupId))
                .build();
    }

    private Long getGroupId(String code) {
        return attendanceCodeService.findGroupIdByCode(code);
    }

    @Override
    public List<ResponseAttendanceMember> findMembers(RequestAttendance requestAttendance) {
        Long groupId = getGroupId(requestAttendance.code());
        String input = requestAttendance.phoneNumber();

        return attendanceRepository.findByGroupMembersPhoneNumberLastFourDigits(groupId, input);
    }

    @Override
    public void saveAttendance(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));
        Group group = member.getGroup();

        attendanceRepository.save(Attendance.builder()
                .group(group)
                .member(member)
                .status(ATTENDANCE)
                .build());
    }

    @Override
    public int calculateAttendanceRate(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_GROUP));
        int totalMemberCount = group.getMembers().size();
        int attendanceMemberCount = attendanceRepository.countByGroupAndStatus(groupId);

        return (attendanceMemberCount * ATTENDANCE_RATE) / totalMemberCount;
    }

}
