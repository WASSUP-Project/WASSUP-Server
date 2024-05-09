package net.skhu.wassup.app.attendance.domain;

import java.util.List;
import net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("""
            SELECT new net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceMember(
            m.id,
            m.name
            )
            FROM Member m
            WHERE SUBSTRING(m.phoneNumber, LENGTH(m.phoneNumber) - 3) = :lastFourDigits
                   AND m.joinStatus = 1
                   AND m.group.id = :groupId
            """)
    List<ResponseAttendanceMember> findByGroupMembersPhoneNumberLastFourDigits(Long groupId, String lastFourDigits);

}
