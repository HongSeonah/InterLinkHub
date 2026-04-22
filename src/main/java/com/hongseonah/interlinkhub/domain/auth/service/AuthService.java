package com.hongseonah.interlinkhub.domain.auth.service;

import com.hongseonah.interlinkhub.domain.auth.dto.request.LoginRequest;
import com.hongseonah.interlinkhub.domain.auth.dto.response.CurrentUserResponse;
import com.hongseonah.interlinkhub.domain.auth.dto.response.LoginResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    CurrentUserResponse getCurrentUser(Authentication authentication);
}