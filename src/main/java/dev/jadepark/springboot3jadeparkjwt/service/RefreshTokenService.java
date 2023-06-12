package dev.jadepark.springboot3jadeparkjwt.service;

import dev.jadepark.springboot3jadeparkjwt.domain.RefreshToken;
import dev.jadepark.springboot3jadeparkjwt.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token : " + refreshToken));
    }
}
