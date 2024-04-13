package net.skhu.wassup.app.group.api;

import static org.springframework.http.HttpStatus.OK;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.group.api.dto.RequestInviteGroup;
import net.skhu.wassup.app.group.service.GroupInviteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/groups/invite")
@Tag(name = "Group Invite Controller", description = "그룹 초대 관련 API")
public class GroupInviteController {

    private final GroupInviteService groupInviteService;

    @PostMapping("send")
    public ResponseEntity<Void> send(Long id, @RequestBody RequestInviteGroup requestInviteGroup) {
        groupInviteService.send(id, requestInviteGroup);

        return ResponseEntity.status(OK).build();
    }

}
