package net.skhu.wassup.app.member.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.skhu.wassup.app.attendance.domain.Attendance;
import net.skhu.wassup.app.common.BaseTimeEntity;
import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.member.api.dto.RequestUpdateMember;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member_table")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "birth", nullable = false)
    private String birth;

    @Column(name = "specifics")
    private String specifics;

    @Enumerated
    @Column(name = "join_status", nullable = false)
    private JoinStatus joinStatus;

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = LAZY)
    private List<Attendance> attendances;

    @Builder
    public Member(Group group, String name, String phoneNumber, String birth, String specifics,
                  JoinStatus joinStatus) {
        this.group = group;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.specifics = specifics;
        this.joinStatus = joinStatus;
    }

    public void update(RequestUpdateMember requestUpdateMember) {
        this.name = (requestUpdateMember.name() != null) ? requestUpdateMember.name() : this.name;
        this.phoneNumber =
                (requestUpdateMember.phoneNumber() != null) ? requestUpdateMember.phoneNumber() : this.phoneNumber;
        this.birth = (requestUpdateMember.birth() != null) ? requestUpdateMember.birth() : this.birth;
        this.specifics = (requestUpdateMember.specifics() != null) ? requestUpdateMember.specifics() : this.specifics;
    }

    public void accept() {
        this.joinStatus = JoinStatus.ACCEPTED;
    }

    @Override
    public String toString() {
        return String.format("id=%d, name='%s'", id, name);
    }

}
