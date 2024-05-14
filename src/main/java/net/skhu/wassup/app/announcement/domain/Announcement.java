package net.skhu.wassup.app.announcement.domain;

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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "announcement_table")
public class Announcement extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "announcement_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Builder
    public Announcement(String title, String content, Group group) {
        this.title = title;
        this.content = content;
        this.group = group;
    }

}
