package com.react.voteproject.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.react.voteproject.dto.CacheRefreshTokenDto;
import com.react.voteproject.exception.CreationException;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class RefreshTokenCache {

    private Cache<Long, String> cache;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public RefreshTokenCache() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)  // 7일 동안 유효한 Refresh Token
                .build();
    }

    public CacheRefreshTokenDto getRefreshToken(Long id) {
        try {
            String cacheRefreshToken = cache.getIfPresent(id);
            return objectMapper.readValue(cacheRefreshToken, CacheRefreshTokenDto.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public void putRefreshToken(Long id, CacheRefreshTokenDto cacheRefreshTokenDto) {
        try {
            // 객체를 JSON 문자열로 변환 후 저장
            String cacheRefreshToken = objectMapper.writeValueAsString(cacheRefreshTokenDto);
            cache.put(id, cacheRefreshToken);
        } catch (JsonProcessingException e) {
            throw new CreationException("JSON 변환 실패");
        }

    }


    public void removeRefreshToken(Long id) {
        cache.invalidate(id);
    }

}
