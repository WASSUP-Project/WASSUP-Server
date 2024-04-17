package net.skhu.wassup.app.group.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

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
        String imageUrl,

        @Schema(description = "총인원")
        int totalMember,

        @Schema(description = "가입대기중인 인원")
        int waitingMember) {

}
