package net.skhu.wassup.app.group.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.admin.domain.Admin;
import net.skhu.wassup.app.admin.domain.AdminRepository;
import net.skhu.wassup.app.certification.CertificationCodeService;
import net.skhu.wassup.app.group.api.dto.RequestGroup;
import net.skhu.wassup.app.group.api.dto.RequestUpdateGroup;
import net.skhu.wassup.app.group.api.dto.ResponseGroup;
import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.group.domain.GroupRepository;
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
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }

        emailMessageSender.send(email, "Wassup 인증번호", certificationCode);
    }

    @Override
    public boolean verify(String email, String inputCode) {
        String certificationCode = certificationCodeService.getCertificationCode(email);

        if (!certificationCode.equals(inputCode)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }

        certificationCodeService.deleteCertificationCode(email);

        return true;
    }

    @Override
    @Transactional
    public void save(Long adminId, RequestGroup requestGroup) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자 정보가 존재하지 않습니다."));

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
                .orElseThrow(() -> new IllegalArgumentException("그룹 정보가 존재하지 않습니다."));
    }

    @Override
    @Transactional
    public List<ResponseGroup> getMyGroup(Long id) {
        return groupRepository.findAllByAdminId(id)
                .stream()
                .map(group -> ResponseGroup.builder()
                        .groupName(group.getName())
                        .groupDescription(group.getDescription())
                        .address(group.getAddress())
                        .businessNumber(group.getBusinessNumber())
                        .email(group.getEmail())
                        .imageUrl(group.getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateGroup(Long id, RequestUpdateGroup requestUpdateGroup, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹 정보가 존재하지 않습니다."));

        if (!group.getAdmin().getId().equals(id)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        group.update(requestUpdateGroup);
    }

    @Override
    @Transactional
    public void deleteGroup(Long id, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹 정보가 존재하지 않습니다."));

        if (!group.getAdmin().getId().equals(id)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        groupRepository.deleteById(groupId);
    }
}
