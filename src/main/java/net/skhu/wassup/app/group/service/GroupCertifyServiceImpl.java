package net.skhu.wassup.app.group.service;

import static net.skhu.wassup.global.error.ErrorCode.INVALID_EMAIL;
import static net.skhu.wassup.global.error.ErrorCode.NOT_MATCH_CERTIFICATION_CODE;

import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.certification.CertificationCodeService;
import net.skhu.wassup.app.group.domain.GroupRepository;
import net.skhu.wassup.global.error.exception.CustomException;
import net.skhu.wassup.global.message.EmailMessageSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupCertifyServiceImpl implements GroupCertifyService {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private final CertificationCodeService certificationCodeService;

    private final EmailMessageSender emailMessageSender;

    private final GroupRepository groupRepository;

    @Override
    public boolean isDuplicateName(String groupName) {
        return groupRepository.existsGroupByName(groupName);
    }

    @Override
    public void certification(String email) {
        String certificationCode = certificationCodeService.getCertificationCode(email);

        if (!email.matches(EMAIL_REGEX)) {
            throw new CustomException(INVALID_EMAIL);
        }

        emailMessageSender.send(email, "Wassup 인증번호", certificationCode);
    }

    @Override
    public boolean verify(String email, String inputCode) {
        String certificationCode = certificationCodeService.getCertificationCode(email);

        if (!certificationCode.equals(inputCode)) {
            throw new CustomException(NOT_MATCH_CERTIFICATION_CODE);
        }

        certificationCodeService.deleteCertificationCode(email);

        return true;
    }

}
