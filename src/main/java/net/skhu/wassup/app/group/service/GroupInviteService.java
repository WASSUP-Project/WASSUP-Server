package net.skhu.wassup.app.group.service;

import net.skhu.wassup.app.group.api.dto.RequestInviteGroup;

public interface GroupInviteService {

    void send(Long adminId, Long groupId, RequestInviteGroup requestInviteGroup);

    void accept(Long adminId, Long memberId);

    void reject(Long adminId, Long memberId);

}
