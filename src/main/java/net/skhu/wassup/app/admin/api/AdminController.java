package net.skhu.wassup.app.admin.api;

import static net.skhu.wassup.global.error.ErrorCode.DUPLICATE_ADMIN_ID;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.admin.api.dto.RequestAdminVerification;
import net.skhu.wassup.app.admin.api.dto.RequestFindPassword;
import net.skhu.wassup.app.admin.api.dto.RequestLogin;
import net.skhu.wassup.app.admin.api.dto.RequestSignup;
import net.skhu.wassup.app.admin.api.dto.RequestVerify;
import net.skhu.wassup.app.admin.api.dto.ResponseAccount;
import net.skhu.wassup.app.admin.api.dto.ResponseAdmin;
import net.skhu.wassup.app.admin.api.dto.ResponseLogin;
import net.skhu.wassup.app.admin.service.AdminService;
import net.skhu.wassup.global.error.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admins")
@Tag(name = "Admin Controller", description = "관리자 로그인 관련 API")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("certification")
    @Operation(
            summary = "인증번호 전송",
            description = "전화번호로 인증번호를 전송합니다."
    )
    public ResponseEntity<Void> certification(@RequestParam String phone) {
        adminService.certification(phone);

        return ResponseEntity.status(OK).build();
    }

    @PostMapping("verify")
    @Operation(
            summary = "인증번호 확인",
            description = "전화번호로 전송된 인증번호를 확인합니다."
    )
    public ResponseEntity<Boolean> verify(@RequestBody RequestVerify requestVerify) {
        return ResponseEntity.status(OK)
                .body(adminService.verify(requestVerify.phoneNumber(), requestVerify.inputCertificationCode()));
    }

    @PostMapping("duplicate")
    @Operation(
            summary = "아이디 중복 확인",
            description = "아이디 중복을 확인합니다."
    )
    public ResponseEntity<Boolean> isDuplicateId(@RequestParam String adminId) {
        if (adminService.isDuplicateId(adminId)) {
            throw new CustomException(DUPLICATE_ADMIN_ID);
        }

        return ResponseEntity.status(OK).body(false);
    }

    @PostMapping("signup")
    @Operation(
            summary = "관리자 회원가입",
            description = "관리자 회원가입을 진행합니다."
    )
    public ResponseEntity<Void> signup(@RequestBody RequestSignup requestSignup) {
        adminService.signup(requestSignup);

        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("login")
    @Operation(
            summary = "관리자 로그인",
            description = "관리자 로그인을 진행합니다."
    )
    public ResponseEntity<ResponseLogin> login(@RequestBody RequestLogin requestLogin) {
        return ResponseEntity.status(OK).body(adminService.login(requestLogin));
    }

    @GetMapping
    @Operation(
            summary = "관리자 조회",
            description = "관리자 정보를 조회합니다."
    )
    public ResponseEntity<ResponseAdmin> getAdmin(Principal principal) {
        Long id = Long.parseLong(principal.getName());

        return ResponseEntity.status(OK).body(adminService.getAdmin(id));
    }

    @PostMapping("find/id")
    @Operation(
            summary = "아이디 찾기",
            description = "전화번호로 관리자 아이디를 찾습니다."
    )
    public ResponseEntity<ResponseAccount> findAdminAccountId(@RequestBody RequestVerify requestVerify) {
        return ResponseEntity.status(OK).body(adminService.findAdminAccountId(requestVerify));
    }

    @PostMapping("find/password/certification")
    @Operation(
            summary = "비밀번호 재설정 인증번호 전송",
            description = "비밀번호 재설정을 위한 인증번호를 전송합니다."
    )
    public ResponseEntity<Void> certificationFindPassword(
            @RequestBody RequestAdminVerification requestAdminVerification) {
        adminService.certificationFindPassword(requestAdminVerification);

        return ResponseEntity.status(OK).build();
    }

    @PutMapping("find/password")
    @Operation(
            summary = "비밀번호 재설정",
            description = "관리자 비밀번호를 재설정합니다."
    )
    public ResponseEntity<Void> updateAdminPassword(@RequestBody RequestFindPassword requestFindPassword) {
        adminService.updateAdminPassword(requestFindPassword);

        return ResponseEntity.status(OK).build();
    }

}
