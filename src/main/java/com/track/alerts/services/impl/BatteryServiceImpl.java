package com.track.alerts.services.impl;

import com.track.alerts.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import com.track.alerts.services.BatteryService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BatteryServiceImpl implements BatteryService {

    @Autowired
    CacheService cacheService;

    @Override
    public void sendBatteryAlert(){

        String sortedData = cacheService.getCacheValue(0,"{RC}ROI76RI76");
        System.out.println(sortedData);
        Set sortedBatteryData = cacheService.getSortedCacheValue(0);
        System.out.println(sortedBatteryData);

        //AlertCountCheck alertCountCheck = cacheService.getLastAlertByType(rootOrgId,tagId,AlertTypeEnum.BATTERY_ALERT_TYPE);
        System.out.println("Hiii");
    }


}
