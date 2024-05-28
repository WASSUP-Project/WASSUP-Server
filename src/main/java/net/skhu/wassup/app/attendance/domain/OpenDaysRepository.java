package net.skhu.wassup.app.attendance.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OpenDaysRepository extends JpaRepository<OpenDays, Long> {

    @Query("""
             SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END
             FROM OpenDays o
                 WHERE o.group.id = :groupId AND o.createDate
                 BETWEEN :startOfDay AND :endOfDay
            """)
    boolean existsByGroupIdAndCreateDate(@Param("groupId") Long groupId,
                                         @Param("startOfDay") LocalDateTime startOfDay,
                                         @Param("endOfDay") LocalDateTime endOfDay);

    default boolean existsByGroupIdAndCreateDate(Long groupId, LocalDate createDate) {
        LocalDateTime startOfDay = createDate.atStartOfDay();
        LocalDateTime endOfDay = createDate.atTime(LocalTime.MAX);

        return existsByGroupIdAndCreateDate(groupId, startOfDay, endOfDay);
    }

}
