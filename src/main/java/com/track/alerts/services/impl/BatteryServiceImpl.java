package com.track.alerts.services.impl;

import com.track.alerts.services.CacheService;
import com.track.alerts.types.LogLevel;
import com.track.alerts.utils.LogUtils;
import com.track.alerts.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import com.track.alerts.services.BatteryService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class BatteryServiceImpl implements BatteryService {

    @Autowired
    CacheService cacheService;

    @Override
    public void sendBatteryAlert(){

        String sortedData = cacheService.getCacheValue(RedisUtils.DB_BRIDGE_CACHE,"{RC}ROI76RI76");
        Set<String> sortedBatteryData = cacheService.getSortedCacheValue(RedisUtils.DB_BRIDGE_CACHE);
        LOGGER.debug(sortedBatteryData.toString());
        LogUtils.logBattery("Testing battery log" , LogLevel.DEBUG);
    }


}
