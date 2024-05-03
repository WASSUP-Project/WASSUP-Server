package net.skhu.wassup.app.group.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "내 그룹 조회")
public record ResponseMyGroup(

        @Schema(description = "그룹 ID")
        Long id,

        @Schema(description = "그룹 이름")
        String name,

        @Schema(description = "그룹 주소")
        String address,

        @Schema(description = "총 인원")
        Long totalMember,

        @Schema(description = "가입 대기중인 인원")
        Long waitingMember,

        @Schema(description = "그룹 이미지 URL")
        String imageUrl) {

}
