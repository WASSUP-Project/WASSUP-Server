package net.skhu.wassup.app.announcement.service;

import static net.skhu.wassup.app.member.domain.JoinStatus.ACCEPTED;
import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_GROUP;
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

    private final SMSMessageSender smsMessageSender;

    private List<Member> getFailedMembers(Group group, RequestAnnouncement requestAnnouncement) {
        return group.getMembers().stream()
                .filter(member -> member.getJoinStatus() == ACCEPTED &&
                        (!isCheckSendStatus(member.getPhoneNumber(), requestAnnouncement.title(),
                                requestAnnouncement.content())))
                .toList();
    }

    private boolean isCheckSendStatus(String phoneNumber, String title, String content) {
        try {
            smsMessageSender.send(phoneNumber, title, content);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
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
