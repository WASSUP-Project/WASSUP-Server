package net.skhu.wassup.app.admin.service;

import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.admin.domain.Admin;
import net.skhu.wassup.app.admin.domain.AdminRepository;
import net.skhu.wassup.app.admin.api.dto.RequestSignup;
import net.skhu.wassup.app.certification.CertificationCodeService;
import net.skhu.wassup.app.encryption.EncryptionService;
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

    @Override
    public boolean isDuplicateId(String adminId) {
        return adminRepository.existsByAdminId(adminId);
    }

    @Override
    public void certification(String phoneNumber) {
        String certificationCode = certificationCodeService.getCertificationCode(phoneNumber);
        System.out.println("인증번호: " + certificationCode);
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

    @Override
    public void login() {
    }

}
