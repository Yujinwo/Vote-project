package com.react.voteproject.service;

import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.CacheRefreshTokenDto;
import com.react.voteproject.dto.RefreshTokenResponseDTO;
import com.react.voteproject.jwt.JwtProvider;
import com.react.voteproject.jwt.RefreshTokenCache;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final JwtProvider jwtProvider;
    private final RefreshTokenCache refresh;

    // refresh token을 이용하여 access token, refresh token 재발급

    public RefreshTokenResponseDTO refreshToken(String ip,String userAgent,String refreshToken) {
        // refresh token id 조회
        Long id = AuthContext.getAuth().getId();
        String role = AuthContext.getAuth().getRole();
        if(id != null) {
            CacheRefreshTokenDto store = refresh.getRefreshToken(id);
            if(store == null) {
                return new RefreshTokenResponseDTO(); // 유효하지 않은 RefreshToken
            }
            else {
                if (!store.getRefreshToken().equals(refreshToken)) {
                    return new RefreshTokenResponseDTO(); // 토큰 불일치 → 탈취 가능성 있음
                }
                if (!store.getIp().equals(ip) || !store.getUserAgent().equals(userAgent)) {
                    return new RefreshTokenResponseDTO(); // IP나 User-Agent 변경 → 의심스러운 접근
                }
            }

            // 새로운 access token 생성
            String newAccessToken = jwtProvider.generateAccessToken(id,role);
           
            // 기존에 가지고 있는 사용자의 refresh token 제거
            refresh.removeRefreshToken(id);

            // 새로운 refresh token 생성 후 저장
            String newRefreshToken = jwtProvider.generateRefreshToken(id,role);
            CacheRefreshTokenDto cacheRefreshTokenDto = CacheRefreshTokenDto.builder().refreshToken(newRefreshToken).ip(ip).userAgent(userAgent).build();
            refresh.putRefreshToken(id,cacheRefreshTokenDto);

            return RefreshTokenResponseDTO.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        }
        else {
            return new RefreshTokenResponseDTO();
        }
    }



}
