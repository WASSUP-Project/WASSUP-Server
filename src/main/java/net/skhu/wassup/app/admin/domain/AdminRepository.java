package net.skhu.wassup.app.admin.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    boolean existsByAdminId(String adminId);
    Admin findByAdminId(String adminId);
}
