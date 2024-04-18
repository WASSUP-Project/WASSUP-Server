package net.skhu.wassup.app.group.service;

import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_GROUP;
import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_MEMBER;
import static net.skhu.wassup.global.error.ErrorCode.UNAUTHORIZED_ADMIN;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.group.api.dto.RequestInviteGroup;
import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.group.domain.GroupRepository;
import net.skhu.wassup.app.member.domain.Member;
import net.skhu.wassup.app.member.domain.MemberRepository;
import net.skhu.wassup.global.error.exception.CustomException;
import net.skhu.wassup.global.message.SMSMessageSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupInviteServiceImpl implements GroupInviteService {

    private final SMSMessageSender smsMessageSender;

    private final GroupRepository groupRepository;

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void send(Long adminId, Long groupId, RequestInviteGroup requestInviteGroup) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_GROUP));

        validateAdmin(adminId, group);

        String code = group.getUniqueCode();
        String message = group.getName() + "/n" + requestInviteGroup.link() + "\n 초대 코드 : " + code;

        smsMessageSender.send(requestInviteGroup.phoneNumber(), "Wassup 초대 코드를 아래 링크에 입력해주세요.", message);
    }

    @Override
    @Transactional
    public void accept(Long id, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));

        Group group = member.getGroup();
        validateAdmin(id, group);

        member.accept();
    }

    @Override
    @Transactional
    public void reject(Long id, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));

        Group group = member.getGroup();
        validateAdmin(id, group);

        memberRepository.deleteById(memberId);
    }

    private void validateAdmin(Long id, Group group) {
        if (!group.getAdmin().getId().equals(id)) {
            throw new CustomException(UNAUTHORIZED_ADMIN);
        }
    }

}
