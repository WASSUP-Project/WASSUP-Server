package net.skhu.wassup.app.attendance.service;

import static net.skhu.wassup.app.attendance.domain.Status.ATTENDANCE;
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
import net.skhu.wassup.app.certification.AttendanceCodeService;
import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.group.domain.GroupRepository;
import net.skhu.wassup.app.member.domain.Member;
import net.skhu.wassup.global.error.exception.CustomException;
import net.skhu.wassup.global.message.SMSMessageSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private static final String SUCCESS_ATTENDANCE_MESSAGE = "%s의 출석이 완료되었습니다.";

    private static final String MESSAGE_PREFIX = "WASSUP_";

    private final AttendanceCodeService attendanceCodeService;

    private final AttendanceRepository attendanceRepository;

    private final GroupRepository groupRepository;

    private final SMSMessageSender smsMessageSender;

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
                .filter(m -> m.getId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));
    }

    private void sendAttendanceMessage(Group group, Member member) {
        String title = MESSAGE_PREFIX + group.getName();
        String message = String.format(SUCCESS_ATTENDANCE_MESSAGE, member.getName());

        smsMessageSender.send(member.getPhoneNumber(), title, message);
    }

    @Override
    @Transactional
    public void saveAttendance(String code, Long memberId) {
        Long groupId = getGroupId(code);
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_GROUP));

        Member member = findMemberById(group, memberId);

        sendAttendanceMessage(group, member);

        attendanceRepository.save(Attendance.builder()
                .group(group)
                .member(member)
                .status(ATTENDANCE)
                .build());
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
