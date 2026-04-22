package com.hongseonah.interlinkhub.domain.auth.dto.response;

import com.hongseonah.interlinkhub.domain.auth.entity.UserRole;

public record CurrentUserResponse(
        String username,
        String displayName,
        UserRole role
) {
}