package net.skhu.wassup.app.announcement.service;

import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_GROUP;
import static net.skhu.wassup.global.error.ErrorCode.UNAUTHORIZED_ADMIN;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.announcement.api.dto.RequestAnnouncement;
import net.skhu.wassup.app.announcement.api.dto.ResponseAnnouncement;
import net.skhu.wassup.app.announcement.domain.Announcement;
import net.skhu.wassup.app.announcement.domain.AnnouncementRepository;
import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.group.domain.GroupRepository;
import net.skhu.wassup.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final GroupRepository groupRepository;

    private final AnnouncementRepository announcementRepository;

    @Override
    @Transactional
    public void writeAnnouncement(Long id, Long groupId, RequestAnnouncement requestAnnouncement) {
        Group group = getGroupOrThrow(groupId);

        if (isNotUserGroupAdmin(id, group)) {
            throw new CustomException(UNAUTHORIZED_ADMIN);
        }

        announcementRepository.save(Announcement.builder()
                .title(requestAnnouncement.title())
                .content(requestAnnouncement.content())
                .group(group)
                .build());
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

    private ResponseAnnouncement mapToResponseAnnouncement(Announcement announcement) {
        return ResponseAnnouncement.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .build();
    }

}
