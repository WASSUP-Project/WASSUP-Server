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
            LEFT JOIN Attendance a
                ON m.id = a.member.id
            WHERE SUBSTRING(m.phoneNumber, LENGTH(m.phoneNumber) - 3) = :lastFourDigits
                  AND m.joinStatus = 1
                  AND a.id IS NULL
                  AND m.group.id = :groupId
            """)
    List<ResponseAttendanceMember> findByGroupMembersPhoneNumberLastFourDigits(Long groupId, String lastFourDigits);

    @Query("""
            SELECT new net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceMember(
                m.id,
                m.name
            )
            FROM Member m
            INNER JOIN Attendance a ON m.id = a.member.id AND a.status = 0
            LEFT JOIN Attendance a2 ON m.id = a2.member.id AND a2.status = 3 AND DATE(a2.createDate) = DATE(NOW())
            WHERE SUBSTRING(m.phoneNumber, LENGTH(m.phoneNumber) - 3) = :lastFourDigits
                  AND m.joinStatus = 1
                  AND a.id IS NOT NULL
                  AND DATE(a.createDate) = DATE(NOW())
                  AND m.group.id = :groupId
                  AND a2.member.id IS NULL
             """)
    List<ResponseAttendanceMember> findByGroupMembersPhoneNumberLastFourDigitsForLeaving(Long groupId,
                                                                                         String lastFourDigits);

    boolean existsByMemberIdAndStatus(Long memberId, Status status);

    @Query("""
            SELECT CASE WHEN COUNT(m.id) = 0 THEN 0
                ELSE (COUNT(CASE WHEN a.status = 0 THEN 1 END) * 100 / COUNT(m.id)) END AS attendance_rate
            FROM Member m
            LEFT JOIN Attendance a
                ON m.id = a.member.id
                AND DATE(a.createDate) = DATE(NOW())
                AND a.status = 0
            WHERE m.group.id = :groupId
                AND m.joinStatus = 1
            """)
    int getAttendanceRateByGroupId(Long groupId);

    @Query("""
            SELECT new net.skhu.wassup.app.attendance.api.dto.ResponseAttendanceMember(
                m.id,
                m.name
            )
            FROM Member m
            LEFT JOIN Attendance a
                ON m.id = a.member.id
            WHERE m.group.id = :groupId
                AND m.joinStatus = 1
                AND a.id IS NULL
            """)
    List<ResponseAttendanceMember> getNotAttendanceMemberByGroupId(Long groupId);

}
