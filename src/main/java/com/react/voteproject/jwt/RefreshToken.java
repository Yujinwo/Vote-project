package com.react.voteproject.jwt;

import com.react.voteproject.service.impl.RedisSingleDataServiceImpl;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class RefreshToken {

    private final RedisSingleDataServiceImpl redisSingleDataService;
    protected static final Map<String, Long> refreshTokens = new HashMap<>();

    /**
     * refresh token get
     *
     * @param refreshToken refresh token
     * @return id
     */
    public Long getRefreshToken(final String refreshToken) {
        return Optional.of(Long.parseLong(redisSingleDataService.getSingleData(refreshToken))).get();
    }


    /**
     * refresh token put
     *
     * @param refreshToken refresh token
     * @param id id
     */
    public void putRefreshToken(final String refreshToken, Long id) {
        Duration duration = Duration.ofHours(24);
        redisSingleDataService.setSingleData(refreshToken, id,duration);
    }

    /**
     * refresh token remove
     *
     * @param refreshToken refresh token
     */
    public void removeRefreshToken(final String refreshToken) {
        redisSingleDataService.deleteSingleData(refreshToken);
    }

    // user refresh token remove
    public void removeUserRefreshToken(final long refreshToken) {
        for(Map.Entry<String, Long> entry : refreshTokens.entrySet()) {
            if(entry.getValue() == refreshToken) {
                removeRefreshToken(entry.getKey());
            }
        }
    }

}
