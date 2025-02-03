package com.react.voteproject.service;

import com.react.voteproject.context.AuthContext;
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
    /**
     * refresh token을 이용하여 access token, refresh token 재발급
     *
     * @param refreshToken refresh token
     * @return RefreshTokenResponseDTO
     */

    public RefreshTokenResponseDTO refreshToken(final String refreshToken,String accessToken) {

        // refresh token id 조회
        Long id = AuthContext.getAuth().getId();
        String role = AuthContext.getAuth().getRole();
        if(id != null) {
            // 유효하지 않은 RefreshToken이면
            if(refresh.getRefreshToken(id) == null) {
                return new RefreshTokenResponseDTO();
            }
            // 새로운 access token 생성
            String newAccessToken = jwtProvider.generateAccessToken(id,role);
           
            // 기존에 가지고 있는 사용자의 refresh token 제거
            refresh.removeRefreshToken(id);

            // 새로운 refresh token 생성 후 저장
            String newRefreshToken = jwtProvider.generateRefreshToken(id,role);
            refresh.putRefreshToken(id,newRefreshToken);

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
