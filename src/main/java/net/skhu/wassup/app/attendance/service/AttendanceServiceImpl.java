package net.skhu.wassup.app.attendance.service;

import static net.skhu.wassup.app.attendance.domain.Status.ABSENCE;
import static net.skhu.wassup.app.attendance.domain.Status.ATTENDANCE;
import static net.skhu.wassup.app.attendance.domain.Status.LEAVING;
import static net.skhu.wassup.app.member.domain.JoinStatus.ACCEPTED;
import static net.skhu.wassup.global.error.ErrorCode.EXPIRED_ATTENDANCE_CODE;
import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_ATTENDANCE;
import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_GROUP;
import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_MEMBER;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceGroupMember;
import net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceInfo;
import net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceMember;
import net.skhu.wassup.app.attendance.api.dto.ResponseCode;
import net.skhu.wassup.app.attendance.domain.Attendance;
import net.skhu.wassup.app.attendance.domain.AttendanceRepository;
import net.skhu.wassup.app.attendance.domain.OpenDays;
import net.skhu.wassup.app.attendance.domain.OpenDaysRepository;
import net.skhu.wassup.app.attendance.domain.Status;
import net.skhu.wassup.app.certification.AttendanceCodeService;
import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.group.domain.GroupRepository;
import net.skhu.wassup.app.member.domain.Member;
import net.skhu.wassup.app.member.domain.MemberRepository;
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

    private final MemberRepository memberRepository;

    private final AttendanceMessageService attendanceMessageService;

    private final OpenDaysRepository openDaysRepository;

    private boolean isOpenDay(Long groupId) {
        return openDaysRepository.existsByGroupIdAndCreateDate(groupId);
    }

    private void saveNewOpenDay(Long groupId) {
        Group group = findGroupById(groupId);
        openDaysRepository.save(OpenDays.builder()
                .group(group)
                .build());
    }

    private void saveOpenDaysIfNotExists(Long groupId) {
        if (!isOpenDay(groupId)) {
            saveNewOpenDay(groupId);
        }
    }

    @Override
    @Transactional
    public ResponseCode generateAttendanceCode(Long groupId) {
        saveOpenDaysIfNotExists(groupId);

        return ResponseCode.builder()
                .code(attendanceCodeService.createCode(groupId))
                .build();
    }

    private boolean isExpiredCode(String code) {
        return attendanceCodeService.findGroupIdByCode(code) == null;
    }

    private Long getGroupId(String code) {
        if (isExpiredCode(code)) {
            throw new CustomException(EXPIRED_ATTENDANCE_CODE);
        }

        return attendanceCodeService.findGroupIdByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseAttendanceMember> findMembers(String code, String phoneNumber) {
        Long groupId = getGroupId(code);

        return attendanceRepository.findByGroupMembersPhoneNumberLastFourDigits(groupId, phoneNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseAttendanceMember> findMembersForLeaving(String code, String phoneNumber) {
        Long groupId = getGroupId(code);

        return attendanceRepository.findByGroupMembersPhoneNumberLastFourDigitsForLeaving(groupId, phoneNumber);
    }

    private Member findMemberById(Group group, Long memberId) {
        return group.getMembers().stream()
                .filter(m -> m.getId().equals(memberId) && m.getJoinStatus() == ACCEPTED)
                .findFirst()
                .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));
    }

    private void sendAttendanceMessage(Group group, Member member, String messageTemplate) {
        attendanceMessageService.sendMessage(group, member, messageTemplate);
    }

    private void saveAttendanceRecord(Group group, Member member, Status status) {
        attendanceRepository.save(Attendance.builder()
                .group(group)
                .member(member)
                .status(status)
                .build());
    }

    private void saveStatus(String code, Long memberId, Status status, String messageTemplate) {
        Long groupId = getGroupId(code);

        Group group = findGroupById(groupId);
        Member member = findMemberById(group, memberId);

        sendAttendanceMessage(group, member, messageTemplate);

        attendanceRepository.findByMemberIdToday(memberId)
                .ifPresentOrElse(attendance -> {
                    if (checkAttendanceStatus(attendance, status)) {
                        attendance.updateStatus(status);
                        return;
                    }
                    throw new CustomException(NOT_FOUND_ATTENDANCE);
                }, () -> saveAttendanceRecord(group, member, status));
    }

    private boolean checkAttendanceStatus(Attendance attendance, Status status) {
        return attendance.getStatus() != status;
    }

    @Override
    @Transactional
    public void saveAttendance(String code, Long memberId) {
        saveStatus(code, memberId, ATTENDANCE, SUCCESS_ATTENDANCE_MESSAGE);
    }

    @Override
    @Transactional
    public void saveLeaving(String code, Long memberId) {
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

    private Status getStatus(Member member) {
        return attendanceRepository.findByMemberIdOrderByCreateDateDesc(member.getId())
                .stream()
                .filter(attendance -> attendance.getCreateDate().toLocalDate().isEqual(LocalDate.now()))
                .findFirst()
                .map(Attendance::getStatus)
                .orElse(ABSENCE);
    }

    private ResponseAttendanceGroupMember convertToResponseAttendanceGroupMember(Member member) {
        return ResponseAttendanceGroupMember.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .status(getStatus(member))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseAttendanceGroupMember> getAttendanceMembers(Long groupId) {
        Group group = findGroupById(groupId);

        return group.getMembers().stream()
                .filter(member -> member.getJoinStatus() == ACCEPTED)
                .map(this::convertToResponseAttendanceGroupMember)
                .toList();
    }

    private Attendance createNewAttendance(Long memberId, Status status) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));
        Group group = member.getGroup();

        return Attendance.builder()
                .member(member)
                .group(group)
                .status(status)
                .build();
    }

    @Override
    @Transactional
    public void updateAttendanceStatus(Long memberId, Status status) {
        Attendance attendance = attendanceRepository.findByMemberIdOrderByCreateDateDesc(memberId)
                .filter(a -> a.getCreateDate().toLocalDate().isEqual(LocalDate.now()))
                .orElseGet(() -> createNewAttendance(memberId, status));

        attendance.updateStatus(status);

        attendanceRepository.save(attendance);
        log.info("Update Attendance Status : {} {}", memberId, status);
    }

    @Override
    @Transactional
    public void updateAllAttendanceStatus(Long groupId, Status status) {
        Group group = findGroupById(groupId);

        List<Member> members = group.getMembers()
                .stream()
                .filter(member -> member.getJoinStatus() == ACCEPTED)
                .toList();

        String messageTemplate = (status == ATTENDANCE) ? SUCCESS_ATTENDANCE_MESSAGE : SUCCESS_LEAVING_MESSAGE;

        for (Member member : members) {
            updateAttendanceStatus(member.getId(), status);
            sendAttendanceMessage(group, member, messageTemplate);
        }
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_GROUP));
    }

}
