package net.skhu.wassup.app.group.service;

import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_ADMIN;
import static net.skhu.wassup.global.error.ErrorCode.NOT_FOUND_GROUP;
import static net.skhu.wassup.global.error.ErrorCode.UNAUTHORIZED_ADMIN;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.admin.domain.Admin;
import net.skhu.wassup.app.admin.domain.AdminRepository;
import net.skhu.wassup.app.certification.GroupUniqueCodeService;
import net.skhu.wassup.app.group.api.dto.RequestGroup;
import net.skhu.wassup.app.group.api.dto.RequestUpdateGroup;
import net.skhu.wassup.app.group.api.dto.ResponseGroup;
import net.skhu.wassup.app.group.api.dto.ResponseMyGroup;
import net.skhu.wassup.app.group.domain.Group;
import net.skhu.wassup.app.group.domain.GroupRepository;
import net.skhu.wassup.app.member.domain.Member;
import net.skhu.wassup.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {


    private final GroupRepository groupRepository;

    private final GroupUniqueCodeService groupUniqueCodeService;


    private final AdminRepository adminRepository;

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
