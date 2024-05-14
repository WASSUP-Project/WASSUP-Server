package net.skhu.wassup.app.announcement.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공지사항 작성 요청")
public record RequestAnnouncement(

        @Schema(description = "제목")
        String title,

        @Schema(description = "내용")
        String content) {

}
