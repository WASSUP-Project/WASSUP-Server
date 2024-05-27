package net.skhu.wassup.app.attendance.service;

import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.member.domain.Member;

public interface AttendanceMessageService {
    void sendMessage(Group group, Member member, String messageTemplate);
}
