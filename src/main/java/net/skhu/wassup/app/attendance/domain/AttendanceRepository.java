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

    @Query("""
            SELECT (COUNT(CASE WHEN a.status = 0 THEN 1 END) / COUNT(*)) * 100 AS attendance_rate
            FROM Member m LEFT JOIN Attendance a
                ON m.id = a.member.id
            WHERE m.group.id = :groupId
            """)
    int getAttendanceRateByGroupId(Long groupId);

    @Query("""
            SELECT new net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceMember(
                m.id,
                m.name)
            FROM Member m
            LEFT JOIN Attendance a
                ON m.id = a.member.id
            WHERE m.group.id = :groupId
            AND a.id IS NULL
            """)
    List<ResponseAttendanceMember> getNotAttendanceMemberByGroupId(Long groupId);

}
