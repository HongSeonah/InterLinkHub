package com.hongseonah.interlinkhub.domain.auth.service;

import com.hongseonah.interlinkhub.domain.auth.dto.request.LoginRequest;
import com.hongseonah.interlinkhub.domain.auth.dto.response.CurrentUserResponse;
import com.hongseonah.interlinkhub.domain.auth.dto.response.LoginResponse;
import com.hongseonah.interlinkhub.domain.auth.entity.AppUser;
import com.hongseonah.interlinkhub.domain.auth.repository.AppUserRepository;
import com.hongseonah.interlinkhub.security.CustomUserDetails;
import com.hongseonah.interlinkhub.security.JwtTokenProvider;
import com.hongseonah.interlinkhub.global.exception.BusinessException;
import com.hongseonah.interlinkhub.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AppUserRepository appUserRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
        } catch (AuthenticationException ex) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        AppUser user = principal.getUser();
        String token = jwtTokenProvider.generateToken(principal);
        return LoginResponse.from(user, token, jwtTokenProvider.getExpirationMinutes());
    }

    @Override
    public CurrentUserResponse getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new CurrentUserResponse(user.getUsername(), user.getDisplayName(), user.getRole());
    }
}