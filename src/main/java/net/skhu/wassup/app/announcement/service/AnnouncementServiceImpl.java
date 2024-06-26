package net.skhu.wassup.app.announcement.service;

import static net.skhu.wassup.app.member.domain.JoinStatus.ACCEPTED;
import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_GROUP;
import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_MEMBER;
import static net.skhu.wassup.global.error.ErrorCode.UNAUTHORIZED_ADMIN;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.skhu.wassup.app.announcement.api.dto.RequestAnnouncement;
import net.skhu.wassup.app.announcement.api.dto.ResponseAnnouncement;
import net.skhu.wassup.app.announcement.domain.Announcement;
import net.skhu.wassup.app.announcement.domain.AnnouncementRepository;
import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.group.domain.GroupRepository;
import net.skhu.wassup.app.member.domain.Member;
import net.skhu.wassup.app.member.domain.MemberRepository;
import net.skhu.wassup.global.error.exception.CustomException;
import net.skhu.wassup.global.message.SMSMessageSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final GroupRepository groupRepository;

    private final AnnouncementRepository announcementRepository;

    private final MemberRepository memberRepository;

    private final SMSMessageSender smsMessageSender;

    private boolean isCheckSendStatus(String phoneNumber, String title, String content) {
        try {
            smsMessageSender.send(phoneNumber, title, content);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private boolean isMemberFailedToReceiveAnnouncement(Member member, RequestAnnouncement requestAnnouncement) {
        return member.getJoinStatus().equals(ACCEPTED) && !isCheckSendStatus(member.getPhoneNumber(),
                requestAnnouncement.title(), requestAnnouncement.content());
    }

    private List<Member> getFailedMembers(Group group, RequestAnnouncement requestAnnouncement) {
        return group.getMembers().stream()
                .filter(member -> isMemberFailedToReceiveAnnouncement(member, requestAnnouncement))
                .toList();
    }

    private void saveAnnouncement(Group group, RequestAnnouncement requestAnnouncement) {
        announcementRepository.save(Announcement.builder()
                .title(requestAnnouncement.title())
                .content(requestAnnouncement.content())
                .group(group)
                .build());
    }

    @Override
    @Transactional
    public List<String> writeAnnouncement(Long id, Long groupId, RequestAnnouncement requestAnnouncement) {
        Group group = getGroupOrThrow(groupId);

        if (isNotUserGroupAdmin(id, group)) {
            throw new CustomException(UNAUTHORIZED_ADMIN);
        }

        List<Member> failedMembers = getFailedMembers(group, requestAnnouncement);

        saveAnnouncement(group, requestAnnouncement);

        return failedMembers.stream()
                .map(Member::toString)
                .toList();
    }

    private boolean isNotGroupMember(Long memberId, Group group) {
        return group.getMembers().stream()
                .noneMatch(member -> member.getId().equals(memberId));
    }

    private String findPhoneNumber(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER))
                .getPhoneNumber();
    }

    @Override
    @Transactional
    public void writeAnnouncementToMember(Long id, Long groupId, Long memberId,
                                          RequestAnnouncement requestAnnouncement) {
        Group group = getGroupOrThrow(groupId);

        if (isNotUserGroupAdmin(id, group)) {
            throw new CustomException(UNAUTHORIZED_ADMIN);
        }

        if (isNotGroupMember(memberId, group)) {
            throw new CustomException(NOT_FOUND_MEMBER);
        }

        String messageTitle = String.format("[%s] %s", group.getName(), requestAnnouncement.title());
        String phoneNumber = findPhoneNumber(memberId);

        isCheckSendStatus(phoneNumber, messageTitle, requestAnnouncement.content());
    }

    private ResponseAnnouncement mapToResponseAnnouncement(Announcement announcement) {
        return ResponseAnnouncement.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseAnnouncement> getAnnouncement(Long id, Long groupId) {
        Group group = getGroupOrThrow(groupId);

        if (isNotUserGroupAdmin(id, group)) {
            throw new CustomException(UNAUTHORIZED_ADMIN);
        }

        return announcementRepository.findByGroupIdOrderByCreateDateDesc(groupId).stream()
                .map(this::mapToResponseAnnouncement)
                .toList();
    }

    private Group getGroupOrThrow(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_GROUP));
    }

    private boolean isNotUserGroupAdmin(Long id, Group group) {
        return !group.getAdmin().getId().equals(id);
    }

}
