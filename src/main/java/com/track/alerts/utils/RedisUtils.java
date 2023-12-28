/**
 * [2014] - [2019] WiSilica Incorporated  All Rights Reserved.
 * NOTICE:  All information contained herein is, and remains the property of WiSilica Incorporated and its suppliers,
 *  if any.  The intellectual and technical concepts contained herein are proprietary to WiSilica Incorporated
 *  and its suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 *  from WiSilica Incorporated.
 **/
package com.track.alerts.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Unnikrishnan A.K <unnikrishnanak@wisilica.com>
 *
 */
public class RedisUtils {
	
	public static final String PACKET_PROCESSING_QUEUE_NAME_PREFIX = "PacketQueue";
	public static final String DELIMETER = ":";
	
	public static final int DB_BRIDGE_CACHE = 0;
	public static final int DB_LISTENER_CACHE = 1;
	public static final int DB_LISTENER_POSITION_MAPPING_CACHE = 2;
	public static final int DB_TAG_CACHE = 3;
	public static final int DB_TAG_INPUT_CACHE = 11;


	public static final int DB_ALERT_CACHE = 16;

	public static final int DB_SENT_ALERT_CACHE = 19;

	public static final int LAST_UPDATED_INFO_CACHE = 20;

	public static final int TAG_EXTRA_DATA_CACHE = 27;

	public static final int DB_TAG_TRIANGULATED_COORDINATES_CACHE = 31;

	public static final int DB_TAG_MAP_DETAILS = 35;
	public static final int SETTING_CACHE = 50;

	public static final int COMMON_CHECK_CACHE = 83;

	public static final int INFANT_CACHE = 84;








	
	public static final String BRIDGE_CACHE_KEY_FORMAT= "CI%d"; // CI<bridgeId>

	public static final String BRIDGE_CACHE_KEY_FORMAT_2= "UT%s"; //UT<token>
	public static final String BRIDGE_CACHE_VALUE_FORMAT= "%s:%s:%d:%d:%s:%s:%s:%s:%s"; // userId:token:rootOrgId:subOrgId:phoneId:networkKey:networkId:version:bridgeIP
	
	public static final String LISTENER_CACHE_KEY_FORMAT= "L%dSO%d"; // L<listenerMeshId>SO<subOrgId>
	public static final String TAG_CACHE_KEY_FORMAT= "T%dRO%d"; // T<tagShortId>RO<rootOrgId>
	
	public static final String TAG_INPUT_CACHE_KEY_FORMAT= "RO%dT%dS%dL%d"; // RO<rootOrgId>T<tagId>s<sequenceNumber>L<listenerId>
	public static final String TAG_INPUT_CACHE_VALUE_FORMAT= "%d:%d:%d:%d:%d:%d:%s:%d:%d:%d:%d:%d:%d:%d"; // rootOrgId:tagId:sequenceNumber:listenerId:rssi:timestamp:battery:tamper:phoneId:temperature:humidity:motion:reserve:isPositioned

	public static final String DB_TAG_TRIANGULATED_COORDINATES_CACHE_KEY_FORMAT= "RO%sT%s"; // T<rootOrgId>RO<tagId>

	public static final String TAG_MAP_DETAILS_CACHE_kEY_FORMAT = "%d";

	public static final String ALERT_CACHE_KEY_FORMAT = "T%sAP%sAG%sTY%s"; //T<tadId>AP<alertPriority>AG<alertGroupId>TY<alertType>

	public static final String ALERT_CACHE_VALUE_FORMAT = "%d:%d:%d:%d:%f:%d:%d:%d:%d:%d:%d:%d:%d"; // alertRuleId:tagId:alertType:timestamp:battery:tamper:alertPriority:subscriptionPriority:timeInterval:alertGroupId:count:listenerId:rssi


	public static final String LAST_UPDATED_INFO_KEY_FORMAT = "%s%s";

	public static final String LAST_UPDATED_INFO_VALUE_FORMAT = "%d:%d";

	public static final String TAG_EXTRA_DATA_CACHE_KEY_FORMAT = DB_TAG_TRIANGULATED_COORDINATES_CACHE_KEY_FORMAT;

	public static final String TAG_EXTRA_DATA_CACHE_VALUE_FORMAT = "%d:%d:%d:%d:%d:%d:%d:%d:%d:%d:%d:%d:%d:%d:%d";  //rootOrgId:TagLongId:battery:tamper:temperature:humidity:motion:batteryChange:tamperChange:TemperatureChange:humidityChange:motionChange:timestamp:lastChangedTimeStamp:nLogged

	public static final String LISTENER_POSITION_MAPPING_CACHE_KEY_FORMAT = "RO%dL%d"; // RO<rootOrgId>L<listenerId>
	public static final String LISTENER_POSITION_MAPPING_CACHE_VALUE_FORMAT = "%s:%f:%f:%d:%d"; // listenerName:lattitude:longitude:layerId:mapLongId

	public static final String LAST_TEMPERATURE_CACHE_KEY_FORMAT = "RO%dT%d"; // RO<rootOrgId>T<tagId>

	public static final String LAST_TEMPERATURE_CACHE_VALUE_FORMAT = "%d"; // temperature

	public static final String LAST_BATTERY_ALERT_CACHE_KEY_FORMAT = "{BATTERY}RO%dT%d"; // {BATTERY}RO<rootOrgId>T<tagId>

	public static final String LAST_BATTERY_ALERT_CACHE_VALUE_FORMAT = "%d:%d:%d:%d"; //<alertType>:<count>:<sequenceNo>:<timestamp>

	public static final String LAST_TAMPER_ALERT_CACHE_KEY_FORMAT = "{TAMPER}RO%dT%d"; // {BATTERY}RO<rootOrgId>T<tagId>

	public static final String LAST_TAMPER_ALERT_CACHE_VALUE_FORMAT = "%d:%d:%d:%d"; //<alertType>:<count>:<sequenceNo>:<timestamp>

	public static final String SETTING_CACHE_BATTERY_KEY_FORMAT = "RO%sSO%sDEFAULT_BATTERY_ALERT_INTERVAL"; //<rootOrgId>:<subOrgId>

	public static final String SETTING_CACHE_TAMPER_KEY_FORMAT = "RO%sSO%sDEFAULT_TAMPER_ALERT_INTERVAL"; //RO<rootOrgId>SO<subOrgId>

	public static final String SETTING_CACHE_VALUE_FORMAT = "%d:%d:%d:%d"; //<rootOrgId>:<subOrgId>:<value>:<status>

	public static final String INFANT_CACHE_KEY_FORMAT = "RO%dT%d"; //<rootOrgId>:<tagId>

	public static final String INFANT_CACHE_VALUE_FORMAT = "%d:%d:%d:%s:%s:%d"; // <tagShortId>:<tagId>:<infantId>:<infantfname>:<infantlname>:<infantLayerId>

	public static final int TRANSACTION_ID_WIDTH = 5;

	public static final String COUTER_QUEUE = "counterQ";


	public static final String getBridgeCacheKey(long bridgeId) {
		return String.format(BRIDGE_CACHE_KEY_FORMAT, bridgeId);
	}

	public static final String getBridgeCacheKey2(String token) {
		return String.format(BRIDGE_CACHE_KEY_FORMAT_2, token);
	}
	
	public static final String getBridgeCacheValue(String userId, String token, long rootOrgId, long subOrgId, String phoneId, String networkKey, String networkId, String version, String bridgeIP) {
		return String.format(BRIDGE_CACHE_VALUE_FORMAT, userId, token, rootOrgId, subOrgId, phoneId, networkKey, networkId, version, bridgeIP);
	}

	public static final String getListenerCacheKey(long subOrgId, long listenerMeshId) {
		return String.format(LISTENER_CACHE_KEY_FORMAT, listenerMeshId, subOrgId);
	}

	public static final String getDbTagTriangulatedCoordinatesCacheKey(long rootOrgId, long tagId) {
		 String paddedRootorgId = StringUtils.leftPad(String.valueOf(rootOrgId), TRANSACTION_ID_WIDTH ,"0");
		String paddedTagId = StringUtils.leftPad(String.valueOf(tagId) ,  TRANSACTION_ID_WIDTH, "0");
		return String.format(DB_TAG_TRIANGULATED_COORDINATES_CACHE_KEY_FORMAT, paddedRootorgId , paddedTagId);
	}

	public static final String GetTagMapDetailsCacheKey(long layerId) {
		return String.valueOf(layerId);
	}

	public static final String getTagCacheKey(long tagShortId, long rootOrgId) {
		return String.format(TAG_CACHE_KEY_FORMAT, tagShortId, rootOrgId);
	}

	public static final String getTagInputCacheKey(long rootOrgId, long tagId, int sequenceNumber, long listenerId) {
		return String.format(TAG_INPUT_CACHE_KEY_FORMAT, rootOrgId, tagId, sequenceNumber, listenerId);
	}

	public static final String getTagInputCacheValue(long rootOrgId, long tagId, int sequenceNumber, long listenerId, int rssi, long timestamp, float battery, int tamper, long phoneId, int temperature, int humidity, int motion, int reserve, int isPositioned) {
		return String.format(RedisUtils.TAG_INPUT_CACHE_VALUE_FORMAT, rootOrgId, tagId, sequenceNumber, listenerId, rssi, timestamp, battery, tamper, phoneId, temperature, humidity, motion, reserve,isPositioned);
	}

	public static final String getAlertCacheKey(long tagId, int alertPriority, int alertGroupId, int alertType) {
		return String.format(RedisUtils.ALERT_CACHE_KEY_FORMAT , padSringWithZero(String.valueOf(tagId)),
				padSringWithZero(String.valueOf(alertPriority)),padSringWithZero(String.valueOf(alertGroupId)),
				padSringWithZero(String.valueOf(alertType)));
	}

	public static final String getAlertCacheValue(int alertRuleId, long tagId, int alertType ,long timestamp,
												  float battery, int tamper, int alertPriority, int subscriptionPriority,
												  long timeInterval, long alertGroupId, int count, long listenerId, int rssi) {
		return String.format(RedisUtils.ALERT_CACHE_VALUE_FORMAT, alertRuleId,tagId,alertType,timestamp,battery,tamper,
				alertPriority,subscriptionPriority,timeInterval,alertGroupId,count,listenerId,rssi);
	}

	public static final String getLastUpdatedInfoCacheKey(char identifier, long id) {

		return String.format(RedisUtils.LAST_UPDATED_INFO_KEY_FORMAT,identifier, padSringWithZero(String.valueOf(id)));
	}

	public static final String getLastUpdatedInfoCacheValue(long id , long timestamp) {
		return String.format(RedisUtils.LAST_UPDATED_INFO_VALUE_FORMAT,id,timestamp);
	}

	public static final String getTagExtraDataCacheKey(long rootOrgId , long tagId) {
		return String.format(RedisUtils.TAG_EXTRA_DATA_CACHE_KEY_FORMAT,rootOrgId , tagId);
	}

	public static final String getTagExtraDataCacheValue(long rootOrgId , long tagId, int battery , int tamper,
															   int temperature, int humidity , int motion , int batteryChange ,
															   int tamperChange,int temperatureChange, int humidityChange, int motionChange, long lastChangedTimestamp,
															   long timeStamp, int logged) {
        return String.format(RedisUtils.TAG_EXTRA_DATA_CACHE_VALUE_FORMAT, rootOrgId,tagId,battery,tamper,temperature,
				humidity,motion,batteryChange,tamperChange,temperatureChange,humidityChange,motionChange,timeStamp,lastChangedTimestamp,logged);
	}

	public static String getListenerPositionMappingCacheKey(long rootOrgId,long listenerId) {
		return String.format(LISTENER_POSITION_MAPPING_CACHE_KEY_FORMAT,rootOrgId,listenerId);
	}

	public static String getLastTemperatureCacheKey(long rootOrgId , long tagId) {
		return String.format(LAST_TEMPERATURE_CACHE_KEY_FORMAT,rootOrgId,tagId);
	}

	public static String getLastTemperatureCacheValue(int temperature) {
		return String.format(LAST_TEMPERATURE_CACHE_VALUE_FORMAT,temperature);
	}

	public static String getLastBatteryAlertCacheKey(long rootOrgId , long tagId) {
		return String.format(LAST_BATTERY_ALERT_CACHE_KEY_FORMAT,rootOrgId,tagId);
	}

	public static String getLastBatteryTamperAlertCacheValue(int alertType, int count, int sequenceNo , long timestamp) {
		return String.format(LAST_BATTERY_ALERT_CACHE_VALUE_FORMAT,alertType,count,sequenceNo,timestamp);
	}

	public static String getLastTamperAlertCacheKey(long rootOrgId , long tagId) {
		return String.format(LAST_TAMPER_ALERT_CACHE_KEY_FORMAT,rootOrgId,tagId);
	}

	public static String getLastTamperAlertCacheValue(int alertType,int count, int sequenceNo , long timestamp) {
		return String.format(LAST_TAMPER_ALERT_CACHE_VALUE_FORMAT,alertType,count,sequenceNo,timestamp);
	}



	public static String getListenerPositionMappingCacheValue(String listenerName,  float lattitude, float longitude, int layerId, long mapLongId) {
		return String.format(LISTENER_POSITION_MAPPING_CACHE_VALUE_FORMAT,listenerName,lattitude,longitude,layerId,mapLongId);
	}

	public static String getSettingCacheBatteryKeyFormat(int rootOrgId , int subOrgId) {
		// giving zero for suborgId base on  old implementation in php BatteryAlertController.php
		return String.format(SETTING_CACHE_BATTERY_KEY_FORMAT , padSringWithZero(String.valueOf(rootOrgId)),padSringWithZero("0"));
	}

	public static String getSettingCacheTamperKeyFormat(int rootOrgId , int subOrgId) {
		// giving zero for suborgId base on  old implementation in php TamperAlertController.php
		return String.format(SETTING_CACHE_TAMPER_KEY_FORMAT , padSringWithZero(String.valueOf(rootOrgId)),padSringWithZero("0"));
	}

	public static String getInfantCacheKeyFormat(int rootOrgId , long tagId) {
		return String.format(INFANT_CACHE_KEY_FORMAT,rootOrgId,tagId);
	}

	
	public static final String DELIMETER_REPLACE_CHAR = "@";
	
	public static final String replaceDelimeter(String value) {
		return StringUtils.isEmpty(value) ? value : value.replace(DELIMETER, DELIMETER_REPLACE_CHAR);
	}

	public static final String reformatValue(String value) {
		return StringUtils.isEmpty(value) ? value : value.replace(DELIMETER_REPLACE_CHAR, DELIMETER);
	}
	
	public static final String stringLeftPad(String string, int size, char padChar) {
		if (null == string) {
			return null;
		}
		return StringUtils.leftPad(string, size, padChar);
	}
	
	public static final String getPacketQueue(String subscriber) {
		return PACKET_PROCESSING_QUEUE_NAME_PREFIX + subscriber;
	}

	public static final String padSringWithZero(String item) {
		return StringUtils.leftPad(item,TRANSACTION_ID_WIDTH,"0");
	}
}
