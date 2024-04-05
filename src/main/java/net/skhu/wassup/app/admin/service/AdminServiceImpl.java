package net.skhu.wassup.app.admin.service;

import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.admin.api.dto.RequestLogin;
import net.skhu.wassup.app.admin.api.dto.ResponseAdmin;
import net.skhu.wassup.app.admin.api.dto.ResponseLogin;
import net.skhu.wassup.app.admin.domain.Admin;
import net.skhu.wassup.app.admin.domain.AdminRepository;
import net.skhu.wassup.app.admin.api.dto.RequestSignup;
import net.skhu.wassup.app.certification.CertificationCodeService;
import net.skhu.wassup.app.encryption.EncryptionService;
import net.skhu.wassup.global.auth.jwt.TokenProvider;
import net.skhu.wassup.global.message.SMSMessageSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    private final CertificationCodeService certificationCodeService;

    private final SMSMessageSender smsMessageSender;

    private final EncryptionService encryptionService;

    private final TokenProvider tokenProvider;

    @Override
    public boolean isDuplicateId(String adminId) {
        return adminRepository.existsByAdminId(adminId);
    }

    @Override
    public void certification(String phoneNumber) {
        String certificationCode = certificationCodeService.getCertificationCode(phoneNumber);
        smsMessageSender.send(phoneNumber, "Wassup 인증번호", certificationCode);
    }

    @Override
    public boolean verify(String phoneNumber, String inputCode) {
        String certificationCode = certificationCodeService.getCertificationCode(phoneNumber);

        if (!ObjectUtils.nullSafeEquals(certificationCode, inputCode)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }

        return true;
    }

    @Override
    @Transactional
    public void signup(RequestSignup requestSignup) {
        String encodePassword = encryptionService.encrypt(requestSignup.password());

        adminRepository.save(Admin.builder()
                .adminId(requestSignup.adminId())
                .password(encodePassword)
                .name(requestSignup.name())
                .phoneNumber(requestSignup.phoneNumber())
                .build());
    }

    private Admin findCertificatedAdmin(RequestLogin requestLogin) {
        Admin admin = adminRepository.findByAdminId(requestLogin.adminId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (ObjectUtils.isEmpty(admin)) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }

        if (!encryptionService.isMatch(requestLogin.password(), admin.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return admin;
    }

    private ResponseLogin createToken(Admin admin) {
        String token = tokenProvider.createToken(admin.getId());

        return ResponseLogin.builder()
                .token(token)
                .build();
    }

    @Override
    @Transactional
    public ResponseLogin login(RequestLogin requestLogin) {
        Admin admin = findCertificatedAdmin(requestLogin);

        return createToken(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseAdmin getAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다."));

        return ResponseAdmin.builder()
                .id(admin.getId())
                .name(admin.getName())
                .phoneNumber(admin.getPhoneNumber())
                .build();
    }

}
