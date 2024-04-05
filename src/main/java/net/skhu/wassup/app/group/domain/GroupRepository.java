package net.skhu.wassup.app.group.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsGroupByName(String groupName);
}
