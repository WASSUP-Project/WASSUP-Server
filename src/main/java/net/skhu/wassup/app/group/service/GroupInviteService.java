package net.skhu.wassup.app.group.service;

import net.skhu.wassup.app.group.api.dto.RequestInviteGroup;

public interface GroupInviteService {

    void send(Long groupId, RequestInviteGroup requestInviteGroup);

    void accept(); // 초대 코드 수락 메서드 (이때 status는 ACCEPTED)

    void reject(); // 초대 코드 거절 메서드 (이때 데이터베이스에서 삭제)

}
