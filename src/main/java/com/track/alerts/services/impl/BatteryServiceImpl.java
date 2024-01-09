package com.track.alerts.services.impl;

import com.track.alerts.services.CacheService;
import com.track.alerts.services.WebClientService;
import com.track.alerts.types.LogLevel;
import com.track.alerts.utils.LogUtils;
import com.track.alerts.utils.RedisUtils;
import com.track.alerts.utils.AlertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import com.track.alerts.services.BatteryService;
import org.springframework.stereotype.Service;
import com.track.alerts.Models.*;
import com.track.alerts.types.AlertTypeEnum;

import java.util.List;
import java.util.Set;
import java.time.Instant;
import java.time.Duration;

@Service
@Slf4j
public class BatteryServiceImpl implements BatteryService {

    @Autowired
    CacheService cacheService;

    @Autowired
    WebClientService<NotificationRequest> webClientService;

    @Override
    public void sendBatteryAlert(){

      //  String sortedData = cacheService.getCacheValue(RedisUtils.DB_BRIDGE_CACHE,"{RC}ROI76RI76");
        List<SortedAlertInfo> sortedBatteryData = cacheService.getSortedCacheValue(RedisUtils.DB_BRIDGE_CACHE);
        for (SortedAlertInfo element : sortedBatteryData){
            long tagId = (null == element) ? 0 :element.getTagId();
            int rootOrgId = (null == element) ? 0 :element.getRootOrgId();
            int subOrgId = (null == element) ? 0 :element.getRootOrgId();

            Double alertSendInfo = cacheService.getAlertSendInfo(tagId,AlertTypeEnum.BATTERY_ALERT_TYPE.getValue());
            long sendTime = alertSendInfo.longValue();
            System.out.print(sendTime);
            Instant sendTimeStamp =  Instant.ofEpochMilli(sendTime);
            Instant currentTimestamp = Instant.now();
            System.out.println(currentTimestamp);
            Duration duration = Duration.between(currentTimestamp, sendTimeStamp);

            long durationSec = duration.getSeconds();

            Settings settings = cacheService.getSettings(rootOrgId,0,AlertTypeEnum.BATTERY_ALERT_TYPE);
            long delay =  AlertUtils.DEFAULT_ALERT_DELAY;
            int status = AlertUtils.DEFAULT_ALERT_STATUS;
            if(null != settings) {
                delay = settings.getValue();
                status = settings.getStatus();
            }

            if((durationSec >= delay && status == AlertUtils.ALERT_STATUS)) {

                NotificationRequest notificationRequest = NotificationRequest.builder()
                        .rootOrgId(String.valueOf(rootOrgId))
                        .subOrgId(String.valueOf(subOrgId))
                        .messageType(AlertTypeEnum.BATTERY_ALERT_TYPE.getValue())
                        .message("'Low Battery Alert'")
                        .chMessage("'低电量警报'")
                        .ruleId("0")
                        .tagId(String.valueOf(tagId))
                        .batteryLevel(String.valueOf(batteryLeftShift2Bit))
                        .infantId(String.valueOf(infant.getInfantId()))
                        .rssi(String.valueOf(rssi))
                        .listenerId(String.valueOf(listenerId))
                        .latitude(String.valueOf(listenerPositionMapping != null ? listenerPositionMapping.getLattitude():0))
                        .longitude(String.valueOf(listenerPositionMapping != null ? listenerPositionMapping.getLongitude() : 0))
                        .infantName(infant.getFName() + " " + infant.getLName())
                        .layerId(String.valueOf(listenerPositionMapping != null ? listenerPositionMapping.getLayerId():0))
                        .mapLongId(String.valueOf(listenerPositionMapping != null ? listenerPositionMapping.getMapLongId():0))
                        .priority(AlertUtils.BATTERY_SUBSCRIPTION_PRIORITY)
                        .groupId("0")
                        .alertPriority("0")
                        .listenerName(listenerPositionMapping != null ? listenerPositionMapping.getListenerName():"Listener not positioned")
                        .infantRoomLayer(String.valueOf(infant.getInfantLayerId()))
                        .build();

                webClientService.post(notificationRequest,notificationUri);

            }



        }
        System.out.println("----------------------");
        //LOGGER.debug(sortedBatteryData.toString());

      //

       // TagInputInfo cachedTagInput = cacheService.getTagInputInfo(0);


      //  LogUtils.logBattery("Testing battery log" , LogLevel.DEBUG);
    }


}
