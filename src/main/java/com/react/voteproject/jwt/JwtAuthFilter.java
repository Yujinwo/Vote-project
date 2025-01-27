package com.react.voteproject.jwt;

import com.react.voteproject.context.AuthContext;
import com.react.voteproject.entity.User;
import com.react.voteproject.repository.UserRepository;
import com.react.voteproject.role.role_enum;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;


    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    // 필터에서 제외할 경로들
    private final List<String> excludedPaths = List.of(
            "/api/auth/refresh",
            "/api/votes/recommend",
            "/api/sessions",
            "/api/votes/summary",
            "/api/votes/hot",
            "/api/uservotes/stats",
            "/api/uservotes/stats/search",
            "/api/votes/all",
            "/api/login",
            "/api/join",
            "/api/error"
    );

    private final List<String> excludedPaths2 = List.of(
            "/api/votes",
            "/api/users"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        String username = null;
        if(isExcludedPath(requestURI)) {
            filterChain.doFilter(request,response);
            return;
        }

        if ("GET".equals(requestMethod)) {
            // GET 메서드에 대한 추가 처리
            if(isExcludedPath2(requestURI)) {
                filterChain.doFilter(request,response);
                return;
            }
        }
        String jwtToken = "";
        // Bearer token 검증 후 user name 조회
        if(token != null && !token.isEmpty()) {
            jwtToken = token.substring(7);
            username = jwtProvider.getUserIdFromToken(jwtToken);
        }

        // token 검증 완료 후 SecurityContextHolder 내 인증 정보가 없는 경우 저장
        if(username != null && !username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Spring Security Context Holder 인증 정보 set
            if(AuthContext.checkAuth())
            {
                String role = (String) jwtProvider.getAllClaimsFromToken(jwtToken).get("role");
                SecurityContextHolder.getContext().setAuthentication(getUserAuth(role));
            }

        }

        filterChain.doFilter(request,response);
    }
    private boolean isExcludedPath(String path) {
        return excludedPaths.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
    private boolean isExcludedPath2(String path) {
        return excludedPaths2.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
    /**
     * token의 사용자 idx를 이용하여 사용자 정보 조회하고, UsernamePasswordAuthenticationToken 생성
     *
     * @return 사용자 UsernamePasswordAuthenticationToken
     */
    private UsernamePasswordAuthenticationToken getUserAuth(String role) {
        User userInfo = AuthContext.getAuth();
        return new UsernamePasswordAuthenticationToken(userInfo.getId(),
                userInfo.getUserPw(),
                Collections.singleton(new SimpleGrantedAuthority(role))
        );
    }

}

