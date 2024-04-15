package net.skhu.wassup.app.group.domain;

import java.util.Optional;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsGroupByName(String groupName);
    boolean existsGroupByUniqueCode(String code);
    Optional<Group> findByUniqueCode(String code);
    Collection<Group> findAllByAdminId(Long id);
}
