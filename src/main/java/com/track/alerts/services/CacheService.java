package com.track.alerts.services;

import com.wisilica.safeall.Models.*;
import com.wisilica.safeall.type.AlertTypeEnum;
import com.wisilica.safeall.utils.CounterUtils;
import com.wisilica.safeall.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CacheService {
    @Value("${counter.path}")
    private String counterPath;

    @Value("${counter.queue.enabled}")
    private Boolean counterQStatus;

    @Value("${counter.file.write.enabled}")
    private Boolean counterFileWriteStatus;

    @Value("${counter.tria.file.write.enabled}")
    private Boolean counterTriaFileWriteStatus;

    @Value(("${tag.temp.exp.seconds}"))
    private int tagTempExpSeconds;

    @Value(("${tag.alert.exp.seconds}"))
    private int tagAlertExpSeconds;
    @Value("${tag.input.cache.expire:10}")
    private int tagInputCacheExpire;

    @Autowired
    @Qualifier("bridgeCache")
    private StringRedisTemplate bridgeCache;

    @Autowired
    @Qualifier("listenerCache")
    private StringRedisTemplate listenerCache;

    @Autowired
    @Qualifier("tagCache")
    private StringRedisTemplate tagCache;

    @Autowired
    @Qualifier("tagInputCache")
    private StringRedisTemplate tagInputCache;

    @Autowired
    @Qualifier("tagTriangulatedCoordinateCache")
    private StringRedisTemplate tagTriangulatedCoordinateCache;

    @Autowired
    @Qualifier("tagMapDetailsCache")
    private StringRedisTemplate tagMapDetailsCache;

    @Autowired
    private StringRedisTemplate alertCache;

    @Autowired
    private StringRedisTemplate alertSendCache;

    @Autowired
    @Qualifier("lastUpdatedInfoCache")
    private StringRedisTemplate lastUpdatedInfoCache;

    @Autowired
    @Qualifier("tagExtraDataCache")
    private StringRedisTemplate tagExtraDataCache;
    @Autowired
    @Qualifier("listenerPositionMappingCache")
    private StringRedisTemplate listenerPositionMappingCache;

    @Autowired
    @Qualifier("lastTemperatureDataCache")
    private StringRedisTemplate lastTemperatureDataCache;

    @Autowired
    private RedisMessagePublisher redisMessagePublisher;

    @Autowired
    private StringRedisTemplate settingDataCache;
    @Autowired
    private StringRedisTemplate infantDataCache;

    public static Map<Long , ExtraTagData> tagLocalCache = new HashMap<>();


    public boolean isValidBridge(int bridgeId) {
        String key = RedisUtils.getBridgeCacheKey(bridgeId);
        return StringUtils.isNotBlank(getCacheValue(RedisUtils.DB_BRIDGE_CACHE, key));
    }

    public BridgeInfo getBridgeInfo(int bridgeId) {
        String key = RedisUtils.getBridgeCacheKey(bridgeId);
        String value = getCacheValue(RedisUtils.DB_BRIDGE_CACHE, key);
        LOGGER.debug("Getting value against bridge : {} >> {}", key, value);
        return BridgeInfo.getObject(value);
    }

    public ListenerInfo getListenerInfo(long subOrgId, int listenerId) {
        String key = RedisUtils.getListenerCacheKey(subOrgId, listenerId);
        String value = getCacheValue(RedisUtils.DB_LISTENER_CACHE, key);
        return ListenerInfo.getObject(value);
    }

    public TagTriangulatedCoordinateInfo getTagTriangulatedCoordinateInfo(long rootOrgId, long tagId) {
        String key = RedisUtils.getDbTagTriangulatedCoordinatesCacheKey(rootOrgId, tagId);
        String value = getCacheValue(RedisUtils.DB_TAG_TRIANGULATED_COORDINATES_CACHE, key);
        return TagTriangulatedCoordinateInfo.getObject(value);
    }

    public TagMapDetailsInfo getTagMapDetailsInfo(long layerId) {
        String key = RedisUtils.GetTagMapDetailsCacheKey(layerId);
        String value = getCacheValue(RedisUtils.DB_TAG_MAP_DETAILS, key);
        return TagMapDetailsInfo.getObject(value);
    }

    public TagInfo getTagInfo(long tagShortId, long rootOrgId) {
        String key = RedisUtils.getTagCacheKey(tagShortId, rootOrgId);
        String value = getCacheValue(RedisUtils.DB_TAG_CACHE, key);
        return TagInfo.getObject(value);
    }

    public TagInputInfo getTagInputInfo(long rootOrgId, long tagId, int sequenceNumber, long listenerId) {
        String key = RedisUtils.getTagInputCacheKey(rootOrgId, tagId, sequenceNumber, listenerId);
        String value = getCacheValue(RedisUtils.DB_TAG_INPUT_CACHE, key);
        return TagInputInfo.getObject(value);
    }

    public ListenerPositionMapping getListenerPositionMapping(long rootOrgId , long listenerId) {
        String key = RedisUtils.getListenerPositionMappingCacheKey(rootOrgId,listenerId);
        String value = getCacheValue(RedisUtils.DB_LISTENER_POSITION_MAPPING_CACHE,key);
        return ListenerPositionMapping.getObject(value);
    }



    public void putTagInputInfo(long rootOrgId, long subOrgId, long tagId, int sequenceNumber, long listenerId, int rssi,
                                long timestamp, int battery, int tamper, long phoneId, int temperature, int humidity,
                                int motion, int reserve, int bridgeId, ListenerInfo listenerInfo,
                                TagTriangulatedCoordinateInfo tagTriangulatedCoordinateInfo,
                                TagMapDetailsInfo tagMapDetailsInfo, int isPositioned , long packetTimestamp , long packetGotTime) {
        String key = RedisUtils.getTagInputCacheKey(rootOrgId, tagId, sequenceNumber, listenerId);
        String value = RedisUtils.getTagInputCacheValue(rootOrgId, tagId, sequenceNumber, listenerId, rssi, timestamp, battery, tamper, phoneId, temperature, humidity, motion, reserve,isPositioned );
        setCacheValue(RedisUtils.DB_TAG_INPUT_CACHE, key, value);
        putTagExtraDataCache(rootOrgId,tagId,battery,tamper,temperature,humidity,motion,reserve ,timestamp,0);
        try {
            StringBuilder counterData = new StringBuilder();
            // |tagId|rssi|sequenceNumber|battery|tamper|timestamp|listenerId|temperature|humidity|motion|reserve|bridgeId|isPositioned|packetTimestamp|CurrentSystemTime|packetGotTime
            counterData.append(tagId).append(",")
                    .append(rssi).append(",")
                    .append(sequenceNumber).append(",")
                    .append(battery).append(",")
                    .append(tamper).append(",")
                    .append(timestamp).append(",")
                    .append(listenerId).append(",")
                    .append(temperature).append(",")
                    .append(humidity).append(",")
                    .append(motion).append(",")
                    .append(reserve).append(",")
                    .append( bridgeId== 0 ? "" : bridgeId).append(",")
                    .append(isPositioned).append(",")
                    .append(packetTimestamp).append(",")
                    .append(System.currentTimeMillis()/1000).append(",")
                    .append(packetGotTime);

            if(counterFileWriteStatus) {
                CounterUtils.createCounterFile(counterData, counterPath);
            }

            /**
             * counter data is pushed only if the counterQstatus is true. This can be changed in application.properties file
             *In the server change value in this file "/opt/subscriber/production_application.properties"
             */
            if(counterQStatus) {
                counterData.deleteCharAt(counterData.indexOf("\n"));
                counterData.append(",").append(rootOrgId).append(",").append(subOrgId);
                redisMessagePublisher.publish(StringUtils.chomp(counterData.toString()));
            }

            if (counterTriaFileWriteStatus && null != tagMapDetailsInfo && null != tagTriangulatedCoordinateInfo) {
                    CounterUtils.createCounterTriaFile(rootOrgId, tagId, sequenceNumber, listenerId, rssi, timestamp, battery,
                            tamper, phoneId, temperature, humidity, motion, reserve, bridgeId, counterPath,
                            listenerInfo, tagTriangulatedCoordinateInfo, tagMapDetailsInfo,isPositioned,packetTimestamp );
            }
        } catch (Exception ex) {
            LOGGER.error("counter file failed to print");
            LOGGER.error(Arrays.toString(ex.getStackTrace()));
        }
    }

    public void putBridgeInfo(int bridgeId, String userId, String token, long rootOrgId, long subOrgId, String phoneId, String networkKey, String networkId, String version, String bridgeIP) {
        String key = RedisUtils.getBridgeCacheKey(bridgeId);
        String value = RedisUtils.getBridgeCacheValue(userId, token, rootOrgId, subOrgId, phoneId, networkKey, networkId, version, bridgeIP);
        LOGGER.debug("Setting bridgeIP : {} and Version : {}", bridgeIP, version);
        LOGGER.debug("key : {} and value : {}", key, value);
        setCacheValue(RedisUtils.DB_BRIDGE_CACHE, key, value);
        key = RedisUtils.getBridgeCacheKey2(token);
        setCacheValue(RedisUtils.DB_BRIDGE_CACHE, key, value);
    }


    public void putAlertInfo(long tagId, int alertPriority, int alertGroupId, int alertRuleId, int alertType, long timestamp,
                             float battery, int tamper, int subscriptionPriority,
                             long timeInterval, int count, long listenerId, int rssi, int bridgeId) {
        String key = RedisUtils.getAlertCacheKey(tagId, alertPriority, alertGroupId, alertType);
        String value = RedisUtils.getAlertCacheValue(alertRuleId, tagId, alertType, timestamp, battery, tamper, alertPriority,
                subscriptionPriority, timeInterval, alertGroupId, count, listenerId, rssi);
        LOGGER.debug("Setting Alert Info for tagId {} from bridgeId {}", tagId, bridgeId);
        LOGGER.debug("key : {} and value : {}", key, value);
        setCacheValue(RedisUtils.DB_ALERT_CACHE, key, value);
    }

    public AlertInfo getAlertSendInfo(long tagId , int alertPriority , int alertGroupId , int alertType) {
        String key = RedisUtils.getAlertCacheKey(tagId,alertPriority,alertGroupId,alertType);
        String value = getCacheValue(RedisUtils.DB_SENT_ALERT_CACHE, key);
        return AlertInfo.getObject(value);

    }

    public void putAlertSentInfo(AlertInfo alertInfo , int bridgeId) {
        String key = RedisUtils.getAlertCacheKey(alertInfo.getTagId(), alertInfo.getAlertPriority(), alertInfo.getAlertGroupId(), alertInfo.getAlertType());
        String value = RedisUtils.getAlertCacheValue(alertInfo.getAlertRuleId(), alertInfo.getTagId(), alertInfo.getAlertType(), alertInfo.getTimestamp(),
                alertInfo.getBattery(), alertInfo.getTamper(), alertInfo.getAlertPriority(),
                alertInfo.getSubscriptionPriority(), alertInfo.getTimeInterval(), alertInfo.getAlertGroupId(), alertInfo.getCount(),
                alertInfo.getListenerId(), alertInfo.getRssi());
        LOGGER.debug("Setting Alert Info for tagId {} from bridgeId {}", alertInfo.getTagId(), bridgeId);
        LOGGER.debug("key : {} and value : {}", key, value);
        setCacheValue(RedisUtils.DB_SENT_ALERT_CACHE, key, value);
    }

    public void putLastUpdatedInfo(char identifier, long id, long timestamp) {

        String key = RedisUtils.getLastUpdatedInfoCacheKey(identifier, id);
        String value = RedisUtils.getLastUpdatedInfoCacheValue(id, timestamp);

        LOGGER.debug("Setting Last Updated Info {} , {} ", identifier, id);
        LOGGER.debug("key : {} and value : {}", key, value);

        setCacheValue(RedisUtils.LAST_UPDATED_INFO_CACHE, key, value);

    }

    public void putTagExtraDataCache(long rootOrgId, long tagId, int battery, int tamper,
                                     int temperature, int humidity, int motion,int reserve ,
                                     long timeStamp, int logged ) {
        ExtraTagData extraTagData = new ExtraTagData(battery,tamper,temperature,humidity,motion,reserve, timeStamp,0,0,0);
        ExtraTagDataChanged extraTagDataChanged = setTagLocalCache(extraTagData , tagId);
        if(extraTagDataChanged.getNoData() == 1 || extraTagDataChanged.getNoChange() != 1) {
            String key = RedisUtils.getTagExtraDataCacheKey(rootOrgId, tagId);
            String value = RedisUtils.getTagExtraDataCacheValue(rootOrgId, tagId, battery, tamper,
                    temperature, humidity, motion, extraTagDataChanged.getBatteryChanged(),
                    extraTagDataChanged.getTamperChanged(),extraTagDataChanged.getTemperatureChanged(), extraTagDataChanged.getHumidityChanged(),
                    extraTagDataChanged.getMotionChanged(), timeStamp,
                    timeStamp, 0);

            LOGGER.debug("Setting Tag Extra Cache for rootOrg ID :  {} , tagId : {}, key : {} and value : {} ", rootOrgId, tagId , key, value);
           // LOGGER.debug("key : {} and value : {}", key, value);
            setCacheValue(RedisUtils.TAG_EXTRA_DATA_CACHE, key, value);
        }


    }

    public  ExtraTagDataChanged setTagLocalCache(ExtraTagData extraTagData , Long tagId) {

        ExtraTagData extraTagDataPrev = tagLocalCache.get(tagId);
        ExtraTagDataChanged extraTagDataChanged = new ExtraTagDataChanged();
        boolean change = false;
       if(null != extraTagDataPrev) {
           if(extraTagDataPrev.getBattery() != extraTagData.getBattery()) {
               extraTagData.setBatteryChanged(1);
               extraTagDataChanged.setBatteryChanged(1);
               //added due to limitation on fetching frequently changed data on tamper and battery update cron
               if(extraTagDataPrev.getLastChangedTimeStamp() == extraTagData.getLastChangedTimeStamp()) {
                   if(extraTagDataPrev.getTemperatureChanged() == 1) {
                       extraTagDataChanged.setTemperatureChanged(1);
                   }
                   if(extraTagDataPrev.getTamper() == 1) {
                       extraTagDataChanged.setTamperChanged(1);
                   }
               }
               change = true;
           }
           if(extraTagDataPrev.getTamper() != extraTagData.getTamper()) {
               extraTagData.setTamperChanged(1);
               extraTagDataChanged.setTamperChanged(1);
               //added due to limitation on fetching frequently changed data on tamper and battery update cron
               if(extraTagDataPrev.getLastChangedTimeStamp() == extraTagData.getLastChangedTimeStamp()) {
                   if(extraTagDataPrev.getBatteryChanged() == 1) {
                       extraTagDataChanged.setBatteryChanged(1);
                   }
                   if(extraTagDataPrev.getTemperatureChanged() == 1) {
                       extraTagDataChanged.setTemperatureChanged(1);
                   }
               }
               change = true;
           }
           if(extraTagDataPrev.getTemperature() != extraTagData.getTemperature()) {
               extraTagData.setTemperatureChanged(1);
               extraTagDataChanged.setTemperatureChanged(1);
               //added due to limitation on fetching frequently changed data on tamper and battery update cron
               if(extraTagDataPrev.getLastChangedTimeStamp() == extraTagData.getLastChangedTimeStamp()) {
                   if(extraTagDataPrev.getBatteryChanged() == 1) {
                       extraTagDataChanged.setBatteryChanged(1);
                   }
                   if(extraTagDataPrev.getTamper() == 1) {
                       extraTagDataChanged.setTamperChanged(1);
                   }
               }
               change = true;
           }
           if(extraTagDataPrev.getMotion() != extraTagData.getMotion()) {
               extraTagDataChanged.setMotionChanged(1);
               change = true;
           }
           if(extraTagDataPrev.getHumidity() != extraTagData.getHumidity()) {
               extraTagDataChanged.setHumidityChanged(1);
               change = true;
           }
           if(extraTagDataPrev.getReserve() != extraTagData.getReserve()) {
               extraTagDataChanged.setReserveChanged(1);
               change = true;
           }
           if(!change) {
               extraTagDataChanged.setNoChange(1);
               extraTagDataChanged.setLastChangedTimeStamp(extraTagDataPrev.getLastChangedTimeStamp());
               return extraTagDataChanged;
           } else {
               extraTagDataChanged.setLastChangedTimeStamp(extraTagData.getLastChangedTimeStamp());
           }

       } else {
           extraTagDataChanged.setNoData(1);
           extraTagDataChanged.setBatteryChanged(1);
           extraTagDataChanged.setTamperChanged(1);
           extraTagDataChanged.setTemperatureChanged(1);
       }

       tagLocalCache.put(tagId , extraTagData);
       return extraTagDataChanged;

    }

    public Integer getLastTemperatureData(int rootOrgId, long tagId) {
        String key = RedisUtils.getLastTemperatureCacheKey(rootOrgId,tagId);
        String value = getCacheValue(RedisUtils.COMMON_CHECK_CACHE,key);
        if(null != value) {
            return Integer.parseInt(value);
        }
        return null;
    }

    public void setLastTemperatureDataCache(int temperature , int rootOrgId , long tagId) {
        String key = RedisUtils.getLastTemperatureCacheKey(rootOrgId, tagId);
        String value = RedisUtils.getLastTemperatureCacheValue(temperature);
        setCacheValue(RedisUtils.COMMON_CHECK_CACHE,key,value);
    }


    public AlertCountCheck getLastAlertByType(int rootOrgId , long tagId , AlertTypeEnum alertType) {
        String key = "";
        switch (alertType) {
            case BATTERY_ALERT_TYPE: key  = RedisUtils.getLastBatteryAlertCacheKey(rootOrgId,tagId); break;
            case TAMPER_ALERT_TYPE: key = RedisUtils.getLastTamperAlertCacheKey(rootOrgId,tagId);break;
            default: return null;
        }
        String value = getCacheValue(RedisUtils.COMMON_CHECK_CACHE,key);
        return AlertCountCheck.getObject(value);
    }

    public void setLastAlert(int rootOrgId, long tagId , AlertCountCheck alertCountCheck , AlertTypeEnum alertType) {
        String key = "";
        switch (alertType) {
            case BATTERY_ALERT_TYPE: key  = RedisUtils.getLastBatteryAlertCacheKey(rootOrgId,tagId); break;
            case TAMPER_ALERT_TYPE: key = RedisUtils.getLastTamperAlertCacheKey(rootOrgId,tagId);break;
            default:return;
        }
        String value = RedisUtils.getLastBatteryTamperAlertCacheValue(alertCountCheck.getAlertType(),alertCountCheck.getCount(),alertCountCheck.getSequenceNo(),alertCountCheck.getTimestamp());
        setCacheValue(RedisUtils.COMMON_CHECK_CACHE,key,value);
    }

    public Settings getSettings(int rootOrgId, int subOrgId, AlertTypeEnum alertType) {
        String key = "";
        switch (alertType) {
            case BATTERY_ALERT_TYPE: key  = RedisUtils.getSettingCacheBatteryKeyFormat(rootOrgId,subOrgId); break;
            case TAMPER_ALERT_TYPE: key = RedisUtils.getSettingCacheTamperKeyFormat(rootOrgId,subOrgId);break;
            default:return null;
        }
        String value = getCacheValue(RedisUtils.SETTING_CACHE,key);
        return Settings.getObject(value);
    }

    public Infant getInfant(int rootOrgId, long tagId) {
        String key = RedisUtils.getInfantCacheKeyFormat(rootOrgId,tagId);
        String value = getCacheValue(RedisUtils.INFANT_CACHE,key);
        return Infant.getObject(value);
    }

    private String getCacheValue(int cacheIndex, String key) {
        switch (cacheIndex) {
            case RedisUtils.DB_BRIDGE_CACHE:
                return bridgeCache.opsForValue().get(key);
            case RedisUtils.DB_TAG_CACHE:
                return tagCache.opsForValue().get(key);
            case RedisUtils.DB_TAG_INPUT_CACHE:
                return tagInputCache.opsForValue().get(key);
            case RedisUtils.DB_LISTENER_CACHE:
                return listenerCache.opsForValue().get(key);
            case RedisUtils.DB_TAG_TRIANGULATED_COORDINATES_CACHE:
                return tagTriangulatedCoordinateCache.opsForValue().get(key);
            case RedisUtils.DB_TAG_MAP_DETAILS:
                return tagMapDetailsCache.opsForValue().get(key);
            case RedisUtils.DB_ALERT_CACHE:
                return alertCache.opsForValue().get(key);
            case RedisUtils.DB_SENT_ALERT_CACHE:
                return alertSendCache.opsForValue().get(key);
            case RedisUtils.DB_LISTENER_POSITION_MAPPING_CACHE:
                return listenerPositionMappingCache.opsForValue().get(key);
            case RedisUtils.COMMON_CHECK_CACHE:
                return lastTemperatureDataCache.opsForValue().get(key);
            case RedisUtils.SETTING_CACHE:
                return settingDataCache.opsForValue().get(key);
            case RedisUtils.INFANT_CACHE:
                return infantDataCache.opsForValue().get(key);
            default:
                return null;
        }
    }

    private void setCacheValue(int cacheIndex, String key, String value) {
        switch (cacheIndex) {
            case RedisUtils.DB_BRIDGE_CACHE:
                bridgeCache.opsForValue().set(key, value);
                break;
            case RedisUtils.DB_TAG_INPUT_CACHE:
                tagInputCache.opsForValue().set(key, value, tagInputCacheExpire, TimeUnit.SECONDS);
                break;
            case RedisUtils.DB_ALERT_CACHE:
                alertCache.opsForValue().set(key, value, 3, TimeUnit.SECONDS);
                break;
            case RedisUtils.DB_SENT_ALERT_CACHE:
                alertSendCache.opsForValue().set(key,value,86400,TimeUnit.SECONDS);
                break;
            case RedisUtils.LAST_UPDATED_INFO_CACHE:
                lastUpdatedInfoCache.opsForValue().set(key, value);
                break;
            case RedisUtils.TAG_EXTRA_DATA_CACHE:
                tagExtraDataCache.opsForValue().set(key,value , 86400, TimeUnit.SECONDS);
                break;
            case RedisUtils.COMMON_CHECK_CACHE: {
                if(key.contains("{")) {
                    lastTemperatureDataCache.opsForValue().set(key,value, tagAlertExpSeconds,TimeUnit.SECONDS);
                } else {
                    lastTemperatureDataCache.opsForValue().set(key,value,tagTempExpSeconds,TimeUnit.SECONDS);

                }
            }break;
            case RedisUtils.SETTING_CACHE:
                 settingDataCache.opsForValue().set(key , value);
                 break;
            case RedisUtils.INFANT_CACHE:
                 infantDataCache.opsForValue().set(key,value);
                 break;
            default:

        }
    }

}
