package net.skhu.wassup.app.group.service;

import static net.skhu.wassup.global.error.ErrorCode.INVALID_EMAIL;
import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_ADMIN;
import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_GROUP;
import static net.skhu.wassup.global.error.ErrorCode.NOT_MATCH_CERTIFICATION_CODE;
import static net.skhu.wassup.global.error.ErrorCode.UNAUTHORIZED_ADMIN;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.admin.domain.Admin;
import net.skhu.wassup.app.admin.domain.AdminRepository;
import net.skhu.wassup.app.certification.CertificationCodeService;
import net.skhu.wassup.app.certification.GroupUniqueCodeService;
import net.skhu.wassup.app.group.api.dto.RequestGroup;
import net.skhu.wassup.app.group.api.dto.RequestUpdateGroup;
import net.skhu.wassup.app.group.api.dto.ResponseGroup;
import net.skhu.wassup.app.group.api.dto.ResponseMyGroup;
import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.group.domain.GroupRepository;
import net.skhu.wassup.app.member.domain.Member;
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

    private final GroupUniqueCodeService groupUniqueCodeService;

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

        String uniqueCode = groupUniqueCodeService.getGroupUniqueCode();

        groupRepository.save(Group.builder()
                .admin(admin)
                .name(requestGroup.groupName())
                .description(requestGroup.groupDescription())
                .address(requestGroup.address())
                .businessNumber(requestGroup.businessNumber())
                .email(requestGroup.email())
                .imageUrl(requestGroup.imageUrl())
                .uniqueCode(uniqueCode)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseGroup getGroup(Long id) {
        return groupRepository.findById(id)
                .map(group -> ResponseGroup.builder()
                        .groupName(group.getName())
                        .groupDescription(group.getDescription())
                        .address(group.getAddress())
                        .businessNumber(group.getBusinessNumber())
                        .email(group.getEmail())
                        .imageUrl(group.getImageUrl())
                        .build())
                .orElseThrow(() -> new CustomException(NOT_FOUND_GROUP));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseMyGroup> getMyGroup(Long id) {
        return groupRepository.findAllByAdminId(id).stream()
                .map(group -> {
                    long totalAcceptedMembers = group.getMembers()
                            .stream()
                            .filter(member -> !member.getIsWaiting())
                            .count();
                    long waitingMembers = group.getMembers()
                            .stream()
                            .filter(Member::getIsWaiting)
                            .count();

                    return ResponseMyGroup.builder()
                            .id(group.getId())
                            .groupName(group.getName())
                            .address(group.getAddress())
                            .totalMember((int) totalAcceptedMembers)
                            .waitingMember((int) waitingMembers)
                            .imageUrl(group.getImageUrl())
                            .build();
                }).collect(Collectors.toList());
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
