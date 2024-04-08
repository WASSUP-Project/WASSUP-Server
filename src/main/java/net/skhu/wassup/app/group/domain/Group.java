package net.skhu.wassup.app.group.domain;

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
import net.skhu.wassup.app.admin.domain.Admin;
import net.skhu.wassup.app.common.BaseTimeEntity;
import net.skhu.wassup.app.group.api.dto.RequestUpdateGroup;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "group_table")
public class Group extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_admin_id")
    private Admin admin;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "business_number")
    private String businessNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "image_url")
    private String imageUrl;

    public void update(RequestUpdateGroup requestUpdateGroup) {
        this.name = requestUpdateGroup.name();
        this.description = requestUpdateGroup.description();
        this.address = requestUpdateGroup.address();
        this.businessNumber = requestUpdateGroup.businessNumber();
        this.imageUrl = requestUpdateGroup.imageUrl();
    }

    @Builder
    public Group(Admin admin, String name, String description, String address, String businessNumber, String email, String imageUrl) {
        this.admin = admin;
        this.name = name;
        this.description = description;
        this.address = address;
        this.businessNumber = businessNumber;
        this.email = email;
        this.imageUrl = imageUrl;
    }

}