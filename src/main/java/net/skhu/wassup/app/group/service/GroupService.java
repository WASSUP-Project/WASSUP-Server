package net.skhu.wassup.app.group.service;

import java.util.List;
import net.skhu.wassup.app.group.api.dto.RequestGroup;
import net.skhu.wassup.app.group.api.dto.RequestUpdateGroup;
import net.skhu.wassup.app.group.api.dto.ResponseGroup;
import net.skhu.wassup.app.group.api.dto.ResponseMyGroup;
import net.skhu.wassup.app.member.api.dto.ResponseMember;

public interface GroupService {

    void saveGroup(Long adminId, RequestGroup requestGroup);

    ResponseGroup getGroup(Long groupId);

    List<ResponseMyGroup> getMyGroups(Long adminId);

    List<ResponseMember> getMemberList(Long adminId, Long groupId, String type);

    void updateGroup(Long adminId, RequestUpdateGroup requestUpdateGroup, Long groupId);

    void deleteGroup(Long adminId, Long groupId);

}
