package net.skhu.wassup.app.group.api;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.group.api.dto.RequestGroup;
import net.skhu.wassup.app.group.api.dto.RequestUpdateGroup;
import net.skhu.wassup.app.group.api.dto.ResponseGroup;
import net.skhu.wassup.app.group.api.dto.ResponseMyGroup;
import net.skhu.wassup.app.group.service.GroupService;
import net.skhu.wassup.app.member.api.dto.ResponseMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/groups")
@Tag(name = "Group Controller", description = "그룹 관련 API")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    @Operation(
            summary = "그룹 생성",
            description = "그룹을 생성합니다."
    )
    public ResponseEntity<Void> createGroup(Principal principal, @RequestBody RequestGroup requestGroup) {
        Long id = Long.parseLong(principal.getName());
        groupService.saveGroup(id, requestGroup);
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping
    @Operation(
            summary = "그룹 정보 조회",
            description = "그룹 정보를 조회합니다."
    )
    @Parameter(name = "id", description = "그룹 ID", required = true)
    public ResponseEntity<ResponseGroup> getGroup(@RequestParam Long id) {
        return ResponseEntity.status(OK).body(groupService.getGroup(id));
    }

    @GetMapping("my")
    @Operation(
            summary = "내 그룹 정보 조회",
            description = "내 그룹 정보를 조회합니다."
    )
    public ResponseEntity<List<ResponseMyGroup>> getMyGroup(Principal principal) {
        Long id = Long.parseLong(principal.getName());
        return ResponseEntity.status(OK).body(groupService.getMyGroups(id));
    }

    @GetMapping("members")
    @Operation(
            summary = "그룹 멤버 조회",
            description = "그룹 멤버를 조회합니다."
    )
    @Parameter(name = "id", description = "그룹 ID", required = true)
    @Parameter(name = "type", description = "멤버 타입 (waiting or accepted or null)")
    public ResponseEntity<List<ResponseMember>> getMemberList(Principal principal, @RequestParam Long id,
                                                              @RequestParam String type) {
        Long adminId = Long.parseLong(principal.getName());
        return ResponseEntity.status(OK).body(groupService.getMemberList(adminId, id, type));
    }

    @PutMapping
    @Operation(
            summary = "그룹 정보 수정",
            description = "그룹 정보를 수정합니다."
    )
    @Parameter(name = "id", description = "그룹 ID", required = true)
    public ResponseEntity<Void> updateGroup(Principal principal,
                                            @RequestBody RequestUpdateGroup requestUpdateGroup,
                                            @RequestParam Long id) {
        Long adminId = Long.parseLong(principal.getName());
        groupService.updateGroup(adminId, requestUpdateGroup, id);
        return ResponseEntity.status(OK).build();
    }

    @DeleteMapping
    @Operation(
            summary = "그룹 삭제",
            description = "그룹을 삭제합니다."
    )
    @Parameter(name = "id", description = "그룹 ID", required = true)
    public ResponseEntity<Void> deleteGroup(Principal principal, @RequestParam Long id) {
        Long adminId = Long.parseLong(principal.getName());
        groupService.deleteGroup(adminId, id);
        return ResponseEntity.status(OK).build();
    }

}
