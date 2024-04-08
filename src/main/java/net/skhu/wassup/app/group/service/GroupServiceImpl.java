package net.skhu.wassup.app.group.service;

import static net.skhu.wassup.global.error.ErrorCode.INVALID_EMAIL;
import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_ADMIN;
import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_GROUP;
import static net.skhu.wassup.global.error.ErrorCode.NOT_MATCH_CERTIFICATION_CODE;
import static net.skhu.wassup.global.error.ErrorCode.UNAUTHORIZED_ADMIN;

import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.admin.domain.Admin;
import net.skhu.wassup.app.admin.domain.AdminRepository;
import net.skhu.wassup.app.certification.CertificationCodeService;
import net.skhu.wassup.app.group.api.dto.RequestGroup;
import net.skhu.wassup.app.group.api.dto.RequestUpdateGroup;
import net.skhu.wassup.app.group.api.dto.ResponseGroup;
import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.group.domain.GroupRepository;
import net.skhu.wassup.global.error.exception.CustomException;
import net.skhu.wassup.global.message.EmailMessageSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private final GroupRepository groupRepository;

    private final CertificationCodeService certificationCodeService;

    private final EmailMessageSender emailMessageSender;

    private final AdminRepository adminRepository;

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

    @Override
    @Transactional
    public void save(Long adminId, RequestGroup requestGroup) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ADMIN));

        groupRepository.save(Group.builder()
                .admin(admin)
                .name(requestGroup.groupName())
                .description(requestGroup.groupDescription())
                .address(requestGroup.address())
                .businessNumber(requestGroup.businessNumber())
                .email(requestGroup.email())
                .imageUrl(requestGroup.imageUrl())
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseGroup getGroup() {

        return groupRepository.findAll().stream()
                .map(ResponseGroup::fromGroup)
                .findFirst()
                .orElseThrow(() -> new CustomException(NOT_FOUND_GROUP));

    }

    @Override
    @Transactional
    public void updateGroup(Long id, RequestUpdateGroup requestUpdateGroup, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_GROUP));

        if (!group.getAdmin().getId().equals(id)) {
            throw new CustomException(UNAUTHORIZED_ADMIN);
        }

        group.update(requestUpdateGroup);
    }

    @Override
    @Transactional
    public void deleteGroup(Long id, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_GROUP));

        if (!group.getAdmin().getId().equals(id)) {
            throw new CustomException(UNAUTHORIZED_ADMIN);
        }

        groupRepository.deleteById(groupId);
    }
}
