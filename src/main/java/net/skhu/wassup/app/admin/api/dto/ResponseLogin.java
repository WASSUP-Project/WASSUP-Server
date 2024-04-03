package net.skhu.wassup.app.admin.api.dto;

import lombok.Builder;

@Builder
public record ResponseLogin(
        String token) {

}
