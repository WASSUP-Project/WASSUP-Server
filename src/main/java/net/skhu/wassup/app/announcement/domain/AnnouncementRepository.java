package net.skhu.wassup.app.announcement.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByGroupId(Long groupId);
}
