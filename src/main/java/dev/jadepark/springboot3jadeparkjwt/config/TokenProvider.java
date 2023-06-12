package dev.jadepark.springboot3jadeparkjwt.config;

import dev.jadepark.springboot3jadeparkjwt.domain.User;
import dev.jadepark.springboot3jadeparkjwt.properties.JwtProperties;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    /**
     * JWT 토큰 생성 메서드
     * @param user
     * @param expiredAt
     * @return
     */
    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    /**
     * JWT 토큰 유효성 검증 메서드
     * @param expiry
     * @param user
     * @return
     */
    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        String token = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)   //헤더 typ : JWT
                //내용
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)   //iat : 현재시간
                .setExpiration(expiry)  //exp : expiry
                .setSubject(user.getEmail())    //sub : 유저이메일
                .claim("id", user.getId())  //id : 유저아이디
                //서명 : 비밀값과 함께 해시값을 HS256방식으로 암호화
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();

        System.out.println("생성된 JWT 토큰, token : " + token);
        return token;
    }

    /**
     * 토큰 기반으로 인증 정보를 가져오는 메서드
     * @param token
     * @return
     */
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())    //secretKey 로 복호화
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e) {  //복호화 과정에서 에러나면 false
            return false;
        }
    }

    /**
     * 토큰 기반으로 인증 정보를 가져오는 메서드
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities)
                , token
                , authorities
        );
    }

    /**
     * 토큰 기반으로 유저 ID를 가져오는 메서드
     * @param token
     * @return
     */
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    /**
     * JWT토큰의 body 가져오는 메서드
     * @param token
     * @return
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

}
