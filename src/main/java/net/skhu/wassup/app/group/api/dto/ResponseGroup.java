package net.skhu.wassup.app.group.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import net.skhu.wassup.app.group.domain.Group;

@Builder
@Schema(description = "그룹 조회")
public record ResponseGroup(

        @Schema(description = "그룹 이름")
        String groupName,

        @Schema(description = "그룹 설명")
        String groupDescription,

        @Schema(description = "그룹 주소")
        String address,

        @Schema(description = "사업자 번호")
        String businessNumber,

        @Schema(description = "이메일")
        String email,

        @Schema(description = "그룹 이미지 URL")
        String imageUrl) {

    public static ResponseGroup fromGroup(Group group) {
        return new ResponseGroup(
                group.getName(),
                group.getDescription(),
                group.getAddress(),
                group.getBusinessNumber(),
                group.getEmail(),
                group.getImageUrl()
        );
    }

}
