package net.skhu.wassup.app.admin.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByAdminId(String adminId);

    Optional<Admin> findByAdminId(String adminId);

    Optional<Admin> findByPhoneNumber(String phoneNumber);

}
