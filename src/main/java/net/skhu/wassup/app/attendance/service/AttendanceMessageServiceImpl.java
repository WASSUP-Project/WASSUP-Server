package net.skhu.wassup.app.attendance.service;

import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.member.domain.Member;
import net.skhu.wassup.global.message.SMSMessageSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceMessageServiceImpl implements AttendanceMessageService {

    private static final String MESSAGE_PREFIX = "WASSUP_";

    private final SMSMessageSender smsMessageSender;

    @Override
    public void sendMessage(Group group, Member member, String messageTemplate) {
        String title = String.format(MESSAGE_PREFIX + group.getName());
        String message = String.format(messageTemplate, member.getName());

        smsMessageSender.send(member.getPhoneNumber(), title, message);
    }
    
}
