package com.react.voteproject.service;

import com.react.voteproject.dto.RefreshTokenResponseDTO;
import com.react.voteproject.jwt.JwtProvider;
import com.react.voteproject.jwt.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final JwtProvider jwtProvider;
    private final RefreshToken refresh;
    /**
     * refresh token을 이용하여 access token, refresh token 재발급
     *
     * @param refreshToken refresh token
     * @return RefreshTokenResponseDTO
     */

    public RefreshTokenResponseDTO refreshToken(final String refreshToken) {

        // refresh token id 조회
        var user_id = refresh.getRefreshToken(refreshToken);
        if(user_id != null) {
            // 새로운 access token 생성
            String newAccessToken = jwtProvider.generateAccessToken(user_id);
           
            // 기존에 가지고 있는 사용자의 refresh token 제거
            refresh.removeRefreshToken(refreshToken);

            // 새로운 refresh token 생성 후 저장
            String newRefreshToken = jwtProvider.generateRefreshToken(user_id);
            refresh.putRefreshToken(newRefreshToken, user_id);

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
