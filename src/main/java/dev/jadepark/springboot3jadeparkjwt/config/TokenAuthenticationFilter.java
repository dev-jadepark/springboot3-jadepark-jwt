package dev.jadepark.springboot3jadeparkjwt.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";   //공백1개포함

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /*
        시큐리티 컨텍스트 : 인증 객체가 저장되는 보관소
        스레드마다 공간을 할당함 -> 스레드 로컬에 저장되므로 아무 곳에서 참조할 수 있고 다른 스레드와 공유하지 않음으로 독립적
        -> 시큐리티 컨텍스트 객체를 저장하는 객체가 시큐리티 컨텍스트 홀더
         */

        if (request.getServletPath().contains("/api/authorized")) { //포함된 경우 무조건 종료
            filterChain.doFilter(request, response);
            return;
        }

        // 요청 헤더의 Authorization key 값 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);

        // 가져온 값의 접두사 제거
        String token = getAccessToken(authorizationHeader);

        // 가져온 토큰이 유효한지 확인하고, 유효한 때는 인증 정보를 설정
        if (tokenProvider.validToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            System.out.println("authentication :: " + authentication );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else {
            // 유효하지 않은 토큰 처리를 위한 코드 추가
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new UnavailableException("accessCode is expired");
        }

        filterChain.doFilter(request, response);
        /*
         Java Servlet API에서 사용되는 인터페이스. 이 인터페이스는 서블릿 필터를 관리하고 연결하는 데 사용
         서블릿 필터의 연속된 체인 -> 체인 형태로 연결되어 순서대로 실행
         요청을 가로채고 수정하거나 응답을 가로채고 수정할 수 있는 작업을 수행 -> 변경된 내용으로 서블릿에 전달하는 역할
         */

        /*
        doFilter(서블릿리쿼스트, 서블릿리스펀스) : 최종적으로 수정된 리쿼스트와 리스펀스를 서블릿에 전달하는 메서드
         */
    }

    /**
     * Bearer 접두사를 제거한 token을 가져온다.
     * @param authorizationHeader
     * @return
     */
    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());    //헤더키가 있으면서 Bearer로 시작하면 length만큼 자름
        }
        return null;
    }
}
