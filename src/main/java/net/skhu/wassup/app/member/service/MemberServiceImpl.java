package net.skhu.wassup.app.member.service;

import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.group.domain.GroupRepository;
import net.skhu.wassup.app.member.api.dto.RequestMember;
import net.skhu.wassup.app.member.domain.JoinStatus;
import net.skhu.wassup.app.member.domain.Member;
import net.skhu.wassup.app.member.domain.MemberRepository;
import net.skhu.wassup.global.error.ErrorCode;
import net.skhu.wassup.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final GroupRepository groupRepository;

    @Override
    @Transactional
    public void save(RequestMember requestMember) {
        Group group = groupRepository.findByUniqueCode(requestMember.groupCode())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GROUP));

        memberRepository.save(Member.builder()
                .name(requestMember.name())
                .phoneNumber(requestMember.phoneNumber())
                .address(requestMember.address())
                .specifics(requestMember.specifics())
                .joinStatus(JoinStatus.WAITING)
                .group(group)
                .build());
    }

}
