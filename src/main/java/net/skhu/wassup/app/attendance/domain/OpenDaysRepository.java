package net.skhu.wassup.app.attendance.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OpenDaysRepository extends JpaRepository<OpenDays, Long> {

    @Query("""
             SELECT COUNT(o) > 0
             FROM OpenDays o
             WHERE o.group.id = :groupId
                 AND DATE(o.createDate) = DATE(NOW())
            """)
    boolean existsByGroupIdAndCreateDate(Long groupId);

}
