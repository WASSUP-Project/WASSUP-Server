package net.skhu.wassup.app.group.api;

import static net.skhu.wassup.global.error.ErrorCode.DUPLICATE_GROUP_NAME;
import static org.springframework.http.HttpStatus.OK;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.group.api.dto.RequestVerify;
import net.skhu.wassup.app.group.service.GroupCertifyService;
import net.skhu.wassup.global.error.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/groups")
@Tag(name = "Group Certify Controller", description = "그룹 인증 관련 API")
public class GroupCertifyController {

    private final GroupCertifyService groupCertifyService;

    @PostMapping("check")
    @Operation(
            summary = "그룹 이름 중복 확인",
            description = "그룹 이름 중복을 확인합니다."
    )
    public ResponseEntity<Boolean> checkGroupName(@RequestParam String groupName) {
        if (groupCertifyService.isDuplicateName(groupName)) {
            throw new CustomException(DUPLICATE_GROUP_NAME);
        }

        return ResponseEntity.status(OK).body(false);
    }

    @PostMapping("certification")
    @Operation(
            summary = "그룹 이메일 인증",
            description = "그룹 이메일 인증을 진행합니다."
    )
    public ResponseEntity<Void> certification(@RequestParam String email) {
        groupCertifyService.certification(email);

        return ResponseEntity.status(OK).build();
    }

    @PostMapping("verify")
    @Operation(
            summary = "그룹 이메일 인증 확인",
            description = "그룹 이메일 인증을 확인합니다."
    )
    public ResponseEntity<Boolean> verify(@RequestBody RequestVerify requestVerify) {
        return ResponseEntity.status(OK)
                .body(groupCertifyService.verify(requestVerify.email(), requestVerify.inputCertificationCode()));
    }

}
