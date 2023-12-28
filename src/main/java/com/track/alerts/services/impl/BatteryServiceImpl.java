package com.track.alerts.services.impl;

import com.track.alert.constants.Constants;
import com.track.alert.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisCacheImpl implements CacheService {

    @Autowired
    @Qualifier(Constants.DEFAULT_REDIS_TEMPLATE)
    StringRedisTemplate defaultRedisTemplate;
    @Override
    public String ping() {
        return defaultRedisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.ping();
            }
        });
    }
}
