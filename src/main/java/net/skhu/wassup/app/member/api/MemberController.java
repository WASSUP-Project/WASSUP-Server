package net.skhu.wassup.app.member.api;

import static org.springframework.http.HttpStatus.CREATED;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.member.api.dto.RequestMember;
import net.skhu.wassup.app.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관리 API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("join")
    public ResponseEntity<Void> join(@RequestBody RequestMember requestMember) {
        memberService.save(requestMember);
        return ResponseEntity.status(CREATED).build();
    }

}
