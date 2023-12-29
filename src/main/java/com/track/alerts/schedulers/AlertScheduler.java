package com.track.alerts.schedulers;

import com.track.alerts.services.BatteryService;
import com.track.alerts.types.LogLevel;
import com.track.alerts.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AlertScheduler {

    @Value("${alert.battery.cron.interval}")
     Long batteryAlertInterval;
    @Value("${alert.tamper.cron.interval}")
    Long tamperAlertInterval;

    @Autowired
    BatteryService batteryService;

    @Scheduled(fixedRateString = "${alert.battery.cron.interval}")
    public void startBatteryAlert()  {
       try{
           LOGGER.debug("Running battery alert on every {} seconds" , batteryAlertInterval / 1000);
           batteryService.sendBatteryAlert();
       } catch (Exception ex) {
           LOGGER.error(ex.getMessage());
           LogUtils.logBattery(ex.getMessage(), LogLevel.ERROR);
       }
    }


    @Scheduled(fixedRateString = "${alert.tamper.cron.interval}")
    public void startTamperAlert(){
        LOGGER.debug("Running tamper alert on every {} seconds" , tamperAlertInterval / 1000);
        //Thread.currentThread().setName("Tamper");
        //System.out.println("Tamper  " +Thread.currentThread().getName());
//		System.out.println("Tamper  " + Thread.currentThread().getName()+System.currentTimeMillis());
        //sortedCache.execute(RedisConnectionCommands::ping);
    }
}
