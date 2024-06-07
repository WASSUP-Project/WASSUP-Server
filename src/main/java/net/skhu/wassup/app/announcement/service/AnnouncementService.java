package net.skhu.wassup.app.announcement.service;

import java.util.List;
import net.skhu.wassup.app.announcement.api.dto.RequestAnnouncement;
import net.skhu.wassup.app.announcement.api.dto.ResponseAnnouncement;

public interface AnnouncementService {

    List<String> writeAnnouncement(Long id, Long groupId, RequestAnnouncement requestAnnouncement);

    List<ResponseAnnouncement> getAnnouncement(Long id, Long groupId);

    void writeAnnouncementToMember(Long id, Long groupId, Long memberId, RequestAnnouncement requestAnnouncement);

}
