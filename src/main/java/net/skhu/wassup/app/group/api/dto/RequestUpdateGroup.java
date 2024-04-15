package net.skhu.wassup.app.group.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "그룹 수정 요청")
public record RequestUpdateGroup(

        @Schema(description = "그룹 설명")
        String description,

        @Schema(description = "그룹 주소")
        String address,

        @Schema(description = "사업자 번호")
        String businessNumber,

        @Schema(description = "그룹 이미지 URL")
        String imageUrl) {

}
