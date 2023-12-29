package com.track.alerts.configurations;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConf {

    @Value("${redis.url}")
    String redishost;

    @Value("${redis.port}")
    int redisport;

    @Value("${redis.password}")
    String redisPassword;

    @Value("${redis.db}")
    Integer redisdb;

    @Bean
    @Primary
    JedisConnectionFactory jedisConnectionFactory(){
        JedisConnectionFactory jedisfactory = new JedisConnectionFactory();
        jedisfactory.setHostName(redishost);
        jedisfactory.setPort(redisport);
        jedisfactory.setDatabase(redisdb);
        if (StringUtils.isNotBlank(redisPassword)) {
            jedisfactory.setPassword(redisPassword);
        }
        return jedisfactory;
    }

    @Bean(name = "sortedCache")
    StringRedisTemplate sortedCache() {
        StringRedisTemplate template = new StringRedisTemplate(jedisConnectionFactory());
        return template;
    }
}
