package net.skhu.wassup.app.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsMemberByIdAndJoinStatus(Long id, JoinStatus joinStatus);
}
