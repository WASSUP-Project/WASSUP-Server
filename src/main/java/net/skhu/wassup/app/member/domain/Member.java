package net.skhu.wassup.app.member.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "specifics")
    private String specifics;

    @Column(name = "join_status", nullable = false)
    private JoinStatus joinStatus;

    @Builder
    public Member(Group group, String name, String phoneNumber, String address, String specifics, JoinStatus joinStatus) {
        this.group = group;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.specifics = specifics;
        this.joinStatus = joinStatus;
    }

    public void accept() {
        this.joinStatus = JoinStatus.ACCEPTED;
    }

    public boolean getIsWaiting() {
        return this.joinStatus == JoinStatus.WAITING;
    }

}
