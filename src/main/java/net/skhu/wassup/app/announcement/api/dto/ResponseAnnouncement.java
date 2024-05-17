package net.skhu.wassup.app.announcement.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "공지사항 조회 응답")
public record ResponseAnnouncement(

        @Schema(description = "공지사항 ID")
        Long id,

        @Schema(description = "제목")
        String title,

        @Schema(description = "내용")
        String content) {

}
