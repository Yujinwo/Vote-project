package com.react.voteproject.jwt;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class RefreshTokenCache {

    private Cache<Long, String> cache;

    public RefreshTokenCache() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)  // 7일 동안 유효한 Refresh Token
                .build();
    }

    public String getRefreshToken(Long id) {
        return cache.getIfPresent(id);
    }

    public void putRefreshToken(Long id,String refreshToken) {
        cache.put(id, refreshToken);
    }


    public void removeRefreshToken(Long id) {
        cache.invalidate(id);
    }

}
