package com.track.alerts.services;

import com.track.alerts.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.track.alerts.Models.*;
import com.track.alerts.types.AlertTypeEnum;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CacheService {

    @Autowired
    @Qualifier("sortedCache")
    private StringRedisTemplate sortedCache;

    public static Map<Long , Settings> settingsLocalCache = new HashMap<>();

    public String getCacheValue(int cacheIndex, String key) {
        switch (cacheIndex) {
            case RedisUtils.DB_BRIDGE_CACHE:
                return sortedCache.opsForValue().get(key);
            default:
                return null;
        }
    }

    public List<SortedAlertInfo> getSortedCacheValue(int cacheIndex) {
        switch (cacheIndex) {
            case RedisUtils.DB_BRIDGE_CACHE:
                Set<String> value = sortedCache.opsForZSet().rangeByScore("low_battery_tags" ,0, 1704178485);
                List<SortedAlertInfo> sortedAlertData = new ArrayList<>();
                for (String element : value){
                    sortedAlertData.add(SortedAlertInfo.getObject(element));
                }
                return sortedAlertData;
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

    public TagInputInfo getTagInputInfo(long tagId) {
        String key = RedisUtils.getTagInputCacheKey(tagId);
        String value = getCacheValue(RedisUtils.DB_TAG_INPUT_CACHE, key);
        return TagInputInfo.getObject(value);
    }

    public Settings getSettings(int rootOrgId, int subOrgId, AlertTypeEnum alertType) {
        Settings settings = settingsLocalCache.get(rootOrgId);
        if (settings == null || System.currentTimeMillis() - settings.getLastChangedTimeStamp() > 600000){
            String key = "";
            switch (alertType) {
                case BATTERY_ALERT_TYPE: key  = RedisUtils.getSettingCacheBatteryKeyFormat(rootOrgId,subOrgId); break;
                case TAMPER_ALERT_TYPE: key = RedisUtils.getSettingCacheTamperKeyFormat(rootOrgId,subOrgId);break;
                default:return null;
            }
            String value = getCacheValue(RedisUtils.SETTING_CACHE,key);
            settings = Settings.getObject(value);
            settingsLocalCache.put((long) rootOrgId,settings);
        }
        return settings;

    }

    public Double getAlertSendInfo(long tagId , int alertType) {
        Double value = sortedCache.opsForZSet().score("repeated_low_bat_tags",String.valueOf(tagId));

        System.out.println("ssss");
        return value;
    }



}
