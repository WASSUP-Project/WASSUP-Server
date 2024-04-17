package net.skhu.wassup.app.group.service;

import java.util.List;
import net.skhu.wassup.app.group.api.dto.RequestGroup;
import net.skhu.wassup.app.group.api.dto.RequestUpdateGroup;
import net.skhu.wassup.app.group.api.dto.ResponseGroup;
import net.skhu.wassup.app.group.api.dto.ResponseMyGroup;

public interface GroupService {

    void save(Long adminId, RequestGroup requestGroup);

    ResponseGroup getGroup(Long groupId);

    List<ResponseMyGroup> getMyGroup(Long id);

    void updateGroup(Long adminId, RequestUpdateGroup requestUpdateGroup, Long groupId);

    void deleteGroup(Long adminId, Long groupId);

}
