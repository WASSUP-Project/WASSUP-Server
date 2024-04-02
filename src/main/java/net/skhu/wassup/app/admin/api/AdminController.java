package net.skhu.wassup.app.admin.api;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.admin.api.dto.RequestSignup;
import net.skhu.wassup.app.admin.api.dto.RequestVerify;
import net.skhu.wassup.app.admin.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admins")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("certification")
    public ResponseEntity<Void> certification(@RequestParam String phone) {
        adminService.certification(phone);

        return ResponseEntity.status(OK).build();
    }

    @PostMapping("verify")
    public ResponseEntity<Boolean> verify(@RequestBody RequestVerify requestVerify) {
        return ResponseEntity.status(OK)
                .body(adminService.verify(requestVerify.phoneNumber(), requestVerify.inputCertificationCode()));
    }

    @PostMapping("duplicate")
    public ResponseEntity<Boolean> isDuplicateId(@RequestParam String id) {
        return ResponseEntity.status(OK).body(adminService.isDuplicateId(id));
    }

    @PostMapping("signup")
    public ResponseEntity<Void> signup(@RequestBody RequestSignup requestSignup) {
        adminService.signup(requestSignup);

        return ResponseEntity.status(CREATED).build();
    }

}
