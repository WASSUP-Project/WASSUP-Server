package net.skhu.wassup.app.admin.domain;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
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
@Table(name = "admin_table")
public class Admin extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    @Column(name = "admin_account_id", nullable = false, unique = true)
    private String adminId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "admin", cascade = REMOVE, fetch = LAZY)
    private List<Group> groups;

    @Builder
    public Admin(String adminId, String password, String name, String phoneNumber) {
        this.adminId = adminId;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public void updatePassword(String encodePassword) {
        this.password = encodePassword;
    }

}
