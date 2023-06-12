package dev.jadepark.springboot3jadeparkjwt.service;

import dev.jadepark.springboot3jadeparkjwt.domain.User;
import dev.jadepark.springboot3jadeparkjwt.config.TokenProvider;
import dev.jadepark.springboot3jadeparkjwt.dto.CreateTokensRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    /**
     * 리프레쉬토큰 유효성 검사 후 새로운 액세스 토큰을 발행하는 메서드
     * @param refreshToken
     * @return
     */
    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token!");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }

    public Map<String, Object> createTokens(CreateTokensRequest request) {

        Map<String, Object> tokens = new HashMap<>();

        User userInfo = userService.findByEmail(request.getEmail());

        if (userInfo == null) {
           throw new UsernameNotFoundException("user not found");

        }

        if (! userInfo.getPassword().matches(request.getPassword())) {
            throw new BadCredentialsException("password 불일치");
        }

        String accessToken = tokenProvider.generateToken(userInfo, Duration.ofHours(2));
        String refreshToken = tokenProvider.generateToken(userInfo, Duration.ofHours(2400));
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }
}
