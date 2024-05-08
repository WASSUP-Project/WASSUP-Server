package net.skhu.wassup.app.attendance.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "출석 요청")
public record RequestAttendance(

        @Schema(description = "출석 코드")
        String code,

        @Schema(description = "휴대폰 번호 뒤 4자리")
        String phoneNumber) {

}
