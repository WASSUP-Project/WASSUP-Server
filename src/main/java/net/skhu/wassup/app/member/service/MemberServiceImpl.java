package net.skhu.wassup.app.member.service;

import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_MEMBER;

import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.group.domain.GroupRepository;
import net.skhu.wassup.app.member.api.dto.RequestMember;
import net.skhu.wassup.app.member.api.dto.ResponseMember;
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
    public void saveMember(RequestMember requestMember) {
        Group group = groupRepository.findByUniqueCode(requestMember.groupCode())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GROUP));

        memberRepository.save(Member.builder()
                .name(requestMember.name())
                .phoneNumber(requestMember.phoneNumber())
                .birth(requestMember.birth())
                .specifics(requestMember.specifics())
                .joinStatus(JoinStatus.WAITING)
                .group(group)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseMember getMember(Long id) {
        return memberRepository.findById(id)
                .map(member -> ResponseMember.builder()
                        .id(member.getId())
                        .name(member.getName())
                        .phoneNumber(member.getPhoneNumber())
                        .birth(member.getBirth())
                        .specifics(member.getSpecifics())
                        .build())
                .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));
    }

}
