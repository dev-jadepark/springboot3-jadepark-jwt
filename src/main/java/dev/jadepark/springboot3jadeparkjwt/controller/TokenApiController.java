package dev.jadepark.springboot3jadeparkjwt.controller;

import dev.jadepark.springboot3jadeparkjwt.domain.User;
import dev.jadepark.springboot3jadeparkjwt.dto.AddUserRequest;
import dev.jadepark.springboot3jadeparkjwt.dto.CreateAccessTokenRequest;
import dev.jadepark.springboot3jadeparkjwt.dto.CreateAccessTokenResponse;
import dev.jadepark.springboot3jadeparkjwt.dto.CreateTokensRequest;
import dev.jadepark.springboot3jadeparkjwt.service.TokenService;
import dev.jadepark.springboot3jadeparkjwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TokenApiController {

    private final TokenService tokenService;
    private final UserService userService;

    @PostMapping("/api/authorized/token/refresh")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse((newAccessToken)));

    }

    @PostMapping("/api/authorized/token")
    public ResponseEntity<Map<String, Object>> createTokens(@RequestBody CreateTokensRequest request) {

        Map<String, Object> tokens = tokenService.createTokens(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tokens);
    }

    @PostMapping("/api/authorized/user")
    public ResponseEntity<Long> createNewUser(@RequestBody AddUserRequest request) {
        Long userId = userService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userId);

    }

    @GetMapping("/api/user/{userId}")
    public ResponseEntity<User> createNewUser(@PathVariable Long userId) {
        User user = userService.findById(userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user);

    }
}
