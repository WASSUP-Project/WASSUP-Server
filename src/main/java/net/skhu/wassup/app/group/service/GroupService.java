package net.skhu.wassup.app.group.service;

import net.skhu.wassup.app.group.api.dto.RequestGroup;
import net.skhu.wassup.app.group.api.dto.RequestUpdateGroup;
import net.skhu.wassup.app.group.api.dto.ResponseGroup;

public interface GroupService {

    boolean isDuplicateName(String groupName);

    void certification(String email);

    boolean verify(String email, String inputCode);

    void save(Long adminId, RequestGroup requestGroup);

    ResponseGroup getGroup();

    void updateGroup(Long adminId, RequestUpdateGroup requestUpdateGroup, Long groupId);

    void deleteGroup(Long adminId, Long groupId);
}
