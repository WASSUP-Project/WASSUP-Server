package net.skhu.wassup.app.attendance.service;

import static net.skhu.wassup.app.attendance.domain.Status.ATTENDANCE;
import static net.skhu.wassup.app.attendance.domain.Status.LEAVING;
import static net.skhu.wassup.app.member.domain.JoinStatus.ACCEPTED;
import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_ATTENDANCE;
import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_GROUP;
import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_MEMBER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceInfo;
import net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceMember;
import net.skhu.wassup.app.attendance.api.dto.ResponseCode;
import net.skhu.wassup.app.attendance.domain.Attendance;
import net.skhu.wassup.app.attendance.domain.AttendanceRepository;
import net.skhu.wassup.app.attendance.domain.Status;
import net.skhu.wassup.app.certification.AttendanceCodeService;
import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.group.domain.GroupRepository;
import net.skhu.wassup.app.member.domain.Member;
import net.skhu.wassup.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private static final String SUCCESS_ATTENDANCE_MESSAGE = "%s(이)가 등원하였습니다.";

    private static final String SUCCESS_LEAVING_MESSAGE = "%s(이)가 하원하였습니다.";

    private final AttendanceCodeService attendanceCodeService;

    private final AttendanceRepository attendanceRepository;

    private final GroupRepository groupRepository;

    private final AttendanceMessageService attendanceMessageService;

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
    @Transactional(readOnly = true)
    public List<ResponseAttendanceMember> findMembers(String code, String phoneNumber) {
        Long groupId = getGroupId(code);

        return attendanceRepository.findByGroupMembersPhoneNumberLastFourDigits(groupId, phoneNumber);
    }

    private Member findMemberById(Group group, Long memberId) {
        return group.getMembers().stream()
                .filter(m -> m.getId().equals(memberId) && m.getJoinStatus() == ACCEPTED)
                .findFirst()
                .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));
    }

    private void saveStatus(String code, Long memberId, Status status, String messageTemplate) {
        Long groupId = getGroupId(code);
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_GROUP));

        Member member = findMemberById(group, memberId);

        attendanceMessageService.sendMessage(group, member, messageTemplate);

        attendanceRepository.save(Attendance.builder()
                .group(group)
                .member(member)
                .status(status)
                .build());
    }

    @Override
    @Transactional
    public void saveAttendance(String code, Long memberId) {
        saveStatus(code, memberId, ATTENDANCE, SUCCESS_ATTENDANCE_MESSAGE);
    }

    @Override
    @Transactional
    public void saveLeaving(String code, Long memberId) {
        if (!attendanceRepository.existsByMemberIdAndStatus(memberId, ATTENDANCE)) {
            throw new CustomException(NOT_FOUND_ATTENDANCE);
        }

        saveStatus(code, memberId, LEAVING, SUCCESS_LEAVING_MESSAGE);
    }

    private int calculateAttendanceRate(Long groupId) {
        return attendanceRepository.getAttendanceRateByGroupId(groupId);
    }

    private List<ResponseAttendanceMember> getNotAttendanceMembers(Long groupId) {
        return attendanceRepository.getNotAttendanceMemberByGroupId(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseAttendanceInfo getAttendanceInfo(Long groupId) {
        return ResponseAttendanceInfo.builder()
                .attendanceRate(calculateAttendanceRate(groupId))
                .notAttendanceMembers(getNotAttendanceMembers(groupId))
                .build();
    }

}
