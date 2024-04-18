package net.skhu.wassup.app.group.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsGroupByName(String groupName);

    boolean existsGroupByUniqueCode(String code);

    Optional<Group> findByUniqueCode(String code);

    List<Group> findAllByAdminId(Long id);
}
