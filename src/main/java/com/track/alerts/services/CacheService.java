package com.track.alerts.services;

import com.track.alerts.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CacheService {

    @Autowired
    @Qualifier("sortedCache")
    private StringRedisTemplate sortedCache;

    public String getCacheValue(int cacheIndex, String key) {
        switch (cacheIndex) {
            case RedisUtils.DB_BRIDGE_CACHE:
                return sortedCache.opsForValue().get(key);
            default:
                return null;
        }
    }

    public Set getSortedCacheValue(int cacheIndex) {
        switch (cacheIndex) {
            case RedisUtils.DB_BRIDGE_CACHE:
                return sortedCache.opsForZSet().range("low_battery_tags" ,0, 3);
            default:
                return null;
        }
    }

    private void setCacheValue(int cacheIndex, String key, String value) {
        switch (cacheIndex) {
            case RedisUtils.DB_BRIDGE_CACHE:
                sortedCache.opsForValue().set(key, value);
                break;
            default:

        }
    }

}
