package net.skhu.wassup.app.admin.service;

import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_ADMIN;
import static net.skhu.wassup.global.error.ErrorCode.NOT_MATCH_ACCOUNT_ID_OR_PASSWORD;
import static net.skhu.wassup.global.error.ErrorCode.NOT_MATCH_ACCOUNT_ID_OR_PHONE_NUMBER;
import static net.skhu.wassup.global.error.ErrorCode.NOT_MATCH_CERTIFICATION_CODE;

import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.admin.api.dto.RequestAdminVerification;
import net.skhu.wassup.app.admin.api.dto.RequestFindPassword;
import net.skhu.wassup.app.admin.api.dto.RequestLogin;
import net.skhu.wassup.app.admin.api.dto.RequestSignup;
import net.skhu.wassup.app.admin.api.dto.RequestVerify;
import net.skhu.wassup.app.admin.api.dto.ResponseAccount;
import net.skhu.wassup.app.admin.api.dto.ResponseAdmin;
import net.skhu.wassup.app.admin.api.dto.ResponseLogin;
import net.skhu.wassup.app.admin.domain.Admin;
import net.skhu.wassup.app.admin.domain.AdminRepository;
import net.skhu.wassup.app.certification.CertificationCodeService;
import net.skhu.wassup.app.encryption.EncryptionService;
import net.skhu.wassup.global.auth.jwt.TokenProvider;
import net.skhu.wassup.global.error.exception.CustomException;
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
            throw new CustomException(NOT_MATCH_CERTIFICATION_CODE);
        }

        certificationCodeService.deleteCertificationCode(phoneNumber);

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
                .orElseThrow(() -> new CustomException(NOT_MATCH_ACCOUNT_ID_OR_PASSWORD));

        if (!encryptionService.isMatch(requestLogin.password(), admin.getPassword())) {
            throw new CustomException(NOT_MATCH_ACCOUNT_ID_OR_PASSWORD);
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
                .orElseThrow(() -> new CustomException(NOT_FOUND_ADMIN));

        int groupCount = admin.getGroups().size();
        int memberCount = admin.getGroups().stream()
                .mapToInt(group -> group.getMembers().size())
                .sum();

        return ResponseAdmin.of(admin.getId(), admin.getName(), admin.getPhoneNumber(), admin.getCreateDate(),
                groupCount, memberCount);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseAccount findAdminAccountId(RequestVerify requestVerify) {
        verify(requestVerify.phoneNumber(), requestVerify.inputCertificationCode());
        Admin admin = adminRepository.findByPhoneNumber(requestVerify.phoneNumber())
                .orElseThrow(() -> new CustomException(NOT_FOUND_ADMIN));

        return ResponseAccount.builder()
                .adminId(admin.getAdminId())
                .build();
    }

    private boolean verifyPhoneNumberAndAdminAccountId(RequestAdminVerification requestAdminVerification) {
        Admin admin = adminRepository.findByAdminId(requestAdminVerification.adminId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_ADMIN));

        if (!ObjectUtils.nullSafeEquals(admin.getPhoneNumber(), requestAdminVerification.phoneNumber())) {
            throw new CustomException(NOT_MATCH_ACCOUNT_ID_OR_PASSWORD);
        }

        return true;
    }

    @Override
    @Transactional
    public void certificationFindPassword(RequestAdminVerification requestAdminVerification) {
        if (!verifyPhoneNumberAndAdminAccountId(requestAdminVerification)) {
            throw new CustomException(NOT_MATCH_ACCOUNT_ID_OR_PHONE_NUMBER);
        }

        certification(requestAdminVerification.phoneNumber());
    }

    @Override
    @Transactional
    public void updateAdminPassword(RequestFindPassword requestFindPassword) {
        Admin admin = adminRepository.findByAdminId(requestFindPassword.adminId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_ADMIN));

        String encodePassword = encryptionService.encrypt(requestFindPassword.newPassword());

        admin.updatePassword(encodePassword);
    }

}
