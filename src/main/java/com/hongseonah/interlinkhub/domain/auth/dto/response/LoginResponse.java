package com.hongseonah.interlinkhub.domain.auth.dto.response;

import com.hongseonah.interlinkhub.domain.auth.entity.AppUser;
import com.hongseonah.interlinkhub.domain.auth.entity.UserRole;
import java.time.LocalDateTime;

public record LoginResponse(
        String tokenType,
        String accessToken,
        String username,
        String displayName,
        UserRole role,
        long expiresInMinutes,
        LocalDateTime issuedAt
) {
    public static LoginResponse from(AppUser user, String token, long expiresInMinutes) {
        return new LoginResponse(
                "Bearer",
                token,
                user.getUsername(),
                user.getDisplayName(),
                user.getRole(),
                expiresInMinutes,
                LocalDateTime.now()
        );
    }
}