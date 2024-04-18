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
import net.skhu.wassup.app.member.api.dto.ResponseMember;
import net.skhu.wassup.app.member.domain.JoinStatus;
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
    public void saveGroup(Long adminId, RequestGroup requestGroup) {
        Admin admin = getAdminOrThrow(adminId);
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
    public ResponseGroup getGroup(Long groupId) {
        Group group = getGroupOrThrow(groupId);

        return mapToResponseGroup(group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseMyGroup> getMyGroups(Long adminId) {
        List<Group> groups = groupRepository.findAllByAdminId(adminId);

        return groups.stream()
                .map(this::mapToResponseMyGroup)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ResponseMember> getMemberList(Long adminId, Long groupId, String type) {
        Group group = getGroupOrThrow(groupId);
        validateAdmin(adminId, group);

        return group.getMembers().stream()
                .filter(member -> filterMembersByType(member, type))
                .map(this::mapToResponseMember)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateGroup(Long id, RequestUpdateGroup requestUpdateGroup, Long groupId) {
        Group group = getGroupOrThrow(groupId);
        validateAdmin(id, group);

        group.update(requestUpdateGroup);
    }

    @Override
    @Transactional
    public void deleteGroup(Long id, Long groupId) {
        Group group = getGroupOrThrow(groupId);
        validateAdmin(id, group);

        groupRepository.deleteById(groupId);
    }

    private Group getGroupOrThrow(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_GROUP));
    }

    private Admin getAdminOrThrow(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ADMIN));
    }

    private void validateAdmin(Long id, Group group) {
        if (!group.getAdmin().getId().equals(id)) {
            throw new CustomException(UNAUTHORIZED_ADMIN);
        }
    }

    private ResponseGroup mapToResponseGroup(Group group) {
        return ResponseGroup.builder()
                .groupName(group.getName())
                .groupDescription(group.getDescription())
                .address(group.getAddress())
                .businessNumber(group.getBusinessNumber())
                .email(group.getEmail())
                .imageUrl(group.getImageUrl())
                .build();
    }

    private ResponseMyGroup mapToResponseMyGroup(Group group) {
        return ResponseMyGroup.builder()
                .id(group.getId())
                .groupName(group.getName())
                .address(group.getAddress())
                .totalMember((int) group.getMembers().stream()
                        .filter(member -> member.getJoinStatus() == JoinStatus.ACCEPTED)
                        .count())
                .waitingMember((int) group.getMembers().stream()
                        .filter(member -> member.getJoinStatus() == JoinStatus.WAITING)
                        .count())
                .imageUrl(group.getImageUrl())
                .build();
    }

    private ResponseMember mapToResponseMember(Member member) {
        return ResponseMember.builder()
                .id(member.getId())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .birth(member.getBirth())
                .specifics(member.getSpecifics())
                .build();
    }

    private boolean filterMembersByType(Member member, String type) {
        return switch (type) {
            case "waiting" -> member.getJoinStatus() == JoinStatus.WAITING;
            case "accepted" -> member.getJoinStatus() == JoinStatus.ACCEPTED;
            default -> true;
        };
    }

}
