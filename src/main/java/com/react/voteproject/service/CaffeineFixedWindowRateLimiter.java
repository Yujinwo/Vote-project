package com.react.voteproject.service;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.react.voteproject.dto.RequestCounter;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CaffeineFixedWindowRateLimiter {

    private final Cache<Object, RequestCounter> cache;
    private static final int MAX_REQUESTS = 5;


    public CaffeineFixedWindowRateLimiter() {
       this.cache = Caffeine.newBuilder()
               .expireAfterWrite(1, TimeUnit.MINUTES)
               .build();

    }

    public boolean isAllowed(String clientIp) {
        RequestCounter counter = cache.get(clientIp, key -> new RequestCounter());

        if (counter.getRequestCount() >= MAX_REQUESTS) {
            return false; // 요청 초과
        }

        counter.setRequestCount(counter.getRequestCount() + 1);
        return true;
    }

}
