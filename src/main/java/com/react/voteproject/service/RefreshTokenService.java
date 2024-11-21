package com.react.voteproject.service;

import com.react.voteproject.dto.RefreshTokenResponseDTO;
import com.react.voteproject.jwt.JwtProvider;
import com.react.voteproject.jwt.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final JwtProvider jwtProvider;

    /**
     * refresh token을 이용하여 access token, refresh token 재발급
     *
     * @param refreshToken refresh token
     * @return RefreshTokenResponseDTO
     */

    public RefreshTokenResponseDTO refreshToken(final String refreshToken) {
        // refresh token 유효성 검증
        if(Boolean.FALSE.equals(jwtProvider.validateToken(refreshToken))) {
            return new RefreshTokenResponseDTO();
        }

        // refresh token id 조회
        var id = RefreshToken.getRefreshToken(refreshToken);

        // 새로운 access token 생성
        String newAccessToken = jwtProvider.generateAccessToken(id);

        // 기존에 가지고 있는 사용자의 refresh token 제거
        RefreshToken.removeUserRefreshToken(id);

        // 새로운 refresh token 생성 후 저장
        String newRefreshToken = jwtProvider.generateRefreshToken(id);
        RefreshToken.putRefreshToken(newRefreshToken, id);

        return RefreshTokenResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }



}
