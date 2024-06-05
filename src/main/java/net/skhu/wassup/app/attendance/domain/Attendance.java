package net.skhu.wassup.app.attendance.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.skhu.wassup.app.common.BaseTimeEntity;
import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.member.domain.Member;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attendance_table")
public class Attendance extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated
    @Column(name = "status", nullable = false)
    private Status status;

    @Builder
    public Attendance(Group group, Member member, Status status) {
        this.group = group;
        this.member = member;
        this.status = status;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

}
