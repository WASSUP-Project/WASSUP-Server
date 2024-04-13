package net.skhu.wassup.app.group.service;

import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.group.api.dto.RequestInviteGroup;
import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.group.domain.GroupRepository;
import net.skhu.wassup.global.error.ErrorCode;
import net.skhu.wassup.global.error.exception.CustomException;
import net.skhu.wassup.global.message.SMSMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupInviteServiceImpl implements GroupInviteService {

    private static final Logger log = LoggerFactory.getLogger(GroupInviteServiceImpl.class);

    private final SMSMessageSender smsMessageSender;

    private final GroupRepository groupRepository;

    @Override
    public void send(Long groupId, RequestInviteGroup requestInviteGroup) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GROUP));

        String code = group.getUniqueCode();
        String message = group.getName() + "/n" + requestInviteGroup.link() + "\n 초대 코드 : " + code;

        smsMessageSender.send(requestInviteGroup.phoneNumber(), "Wassup 초대 코드를 아래 링크에 입력해주세요.", message);

        log.info("그룹 초대 코드 전송: Group={}, Phone={}, Code={}", group.getName(), requestInviteGroup.phoneNumber(), code);
    }

    @Override
    public void accept() {

    }

    @Override
    public void reject() {

    }

}
