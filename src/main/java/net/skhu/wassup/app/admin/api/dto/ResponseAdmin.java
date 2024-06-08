package net.skhu.wassup.app.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
@Schema(description = "관리자 조회")
public record ResponseAdmin(

        @Schema(description = "관리자 ID")
        Long id,

        @Schema(description = "관리자 이름")
        String name,

        @Schema(description = "전화번호")
        String phoneNumber,

        @Schema(description = "생성일")
        String createdAt,

        @Schema(description = "그룹 수")
        int groupCount,

        @Schema(description = "멤버 수")
        int memberCount) {

    public static ResponseAdmin of(Long id, String name, String phoneNumber, LocalDateTime createdAt, int groupCount,
                                   int memberCount) {
        return ResponseAdmin.builder()
                .id(id)
                .name(name)
                .phoneNumber(phoneNumber)
                .createdAt(createdAt.toLocalDate().toString())
                .groupCount(groupCount)
                .memberCount(memberCount)
                .build();
    }

}
