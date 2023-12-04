package com.azry.lms.config;

import com.azry.lms.util.constant.RateLimiterConstant;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitingConfig {
    @Bean
    @SuppressWarnings("UnstableApiUsage") // RateLimiter marked with @Beta
    public RateLimiter rateLimiter() {
        return RateLimiter.create(RateLimiterConstant.PERMITS_PER_SECOND);
    }
}
