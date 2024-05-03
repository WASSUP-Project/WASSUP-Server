package net.skhu.wassup.app.group.domain;

import java.util.List;
import java.util.Optional;
import net.skhu.wassup.app.group.api.dto.ResponseMyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupRepository extends JpaRepository<Group, Long> {

    boolean existsGroupByName(String groupName);

    boolean existsGroupByUniqueCode(String code);

    Optional<Group> findByUniqueCode(String code);

    @Query(value = """
            SELECT new net.skhu.wassup.app.group.api.dto.ResponseMyGroup(
                g.id,
                g.name,
                g.address,
                COUNT(CASE WHEN m.joinStatus = 1 THEN 1 END),
                COUNT(CASE WHEN m.joinStatus = 0 THEN 1 END),
                g.imageUrl)
            FROM Group g LEFT JOIN Member m
                ON g.id = m.group.id
            WHERE g.admin.id = :adminId
            GROUP BY g.id
            """)
    List<ResponseMyGroup> getMyGroups(@Param("adminId") Long adminId);

}
