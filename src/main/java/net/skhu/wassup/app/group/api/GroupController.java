package net.skhu.wassup.app.group.api;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.group.api.dto.RequestGroup;
import net.skhu.wassup.app.group.api.dto.RequestUpdateGroup;
import net.skhu.wassup.app.group.api.dto.RequestVerify;
import net.skhu.wassup.app.group.api.dto.ResponseGroup;
import net.skhu.wassup.app.group.service.GroupService;
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

    @PostMapping("check")
    @Operation(
            summary = "그룹 이름 중복 확인",
            description = "그룹 이름 중복을 확인합니다."
    )
    public ResponseEntity<Boolean> checkGroupName(@RequestParam String groupName) {
        return ResponseEntity.status(OK).body(groupService.isDuplicateName(groupName));
    }

    @PostMapping("certification")
    @Operation(
            summary = "그룹 이메일 인증",
            description = "그룹 이메일 인증을 진행합니다."
    )
    public ResponseEntity<Void> certification(@RequestParam String email) {
        groupService.certification(email);

        return ResponseEntity.status(OK).build();
    }

    @PostMapping("verify")
    @Operation(
            summary = "그룹 이메일 인증 확인",
            description = "그룹 이메일 인증을 확인합니다."
    )
    public ResponseEntity<Boolean> verify(@RequestBody RequestVerify requestVerify) {
        return ResponseEntity.status(OK)
                .body(groupService.verify(requestVerify.email(), requestVerify.inputCertificationCode()));
    }

    @PostMapping
    @Operation(
            summary = "그룹 생성",
            description = "그룹을 생성합니다."
    )
    public ResponseEntity<Void> createGroup(Principal principal, @RequestBody RequestGroup requestGroup) {
        Long id = Long.parseLong(principal.getName());
        groupService.save(id, requestGroup);
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping
    @Operation(
            summary = "그룹 정보 조회",
            description = "그룹 정보를 조회합니다."
    )
    public ResponseEntity<ResponseGroup> getGroup() {
        return ResponseEntity.status(OK).body(groupService.getGroup());
    }

    @PutMapping
    @Operation(
            summary = "그룹 정보 수정",
            description = "그룹 정보를 수정합니다."
    )
    public ResponseEntity<Void> updateGroup(Principal principal,
                                                     @RequestBody RequestUpdateGroup requestUpdateGroup,
                                                     @RequestParam Long groupId) {
        Long id = Long.parseLong(principal.getName());
        groupService.updateGroup(id, requestUpdateGroup, groupId);
        return ResponseEntity.status(OK).build();
    }

    @DeleteMapping
    @Operation(
            summary = "그룹 삭제",
            description = "그룹을 삭제합니다."
    )
    public ResponseEntity<Void> deleteGroup(Principal principal, @RequestParam Long groupId) {
        Long id = Long.parseLong(principal.getName());
        groupService.deleteGroup(id, groupId);
        return ResponseEntity.status(OK).build();
    }

}
