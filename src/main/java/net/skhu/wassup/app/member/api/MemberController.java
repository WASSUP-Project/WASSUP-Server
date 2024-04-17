package net.skhu.wassup.app.member.api;

import static javax.security.auth.callback.ConfirmationCallback.OK;
import static org.springframework.http.HttpStatus.CREATED;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.member.api.dto.RequestMember;
import net.skhu.wassup.app.member.api.dto.ResponseMember;
import net.skhu.wassup.app.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관리 API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("join")
    @Operation(
            summary = "가입 요청",
            description = "그룹에 가입을 요청합니다"
    )
    public ResponseEntity<Void> join(@RequestBody RequestMember requestMember) {
        memberService.save(requestMember);
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping
    @Operation(
            summary = "멤버 조회",
            description = "멤버를 조회합니다"
    )
    @Parameter(name = "id", description = "멤버 ID")
    public ResponseEntity<ResponseMember> findById(@RequestParam Long id) {
        return ResponseEntity.status(OK).body(memberService.getMember(id));
    }

}
