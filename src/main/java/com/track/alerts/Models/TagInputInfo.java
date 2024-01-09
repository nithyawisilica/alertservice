package com.track.alerts.Models;

import com.track.alerts.utils.RedisUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagInputInfo {
	
	private String tagId;
	private long rootOrgId;
	private int sequenceNumber;
	private long listenerId;
	private int rssi;
	private long timestamp;
	private float battery;
	private int tamper;
	private long phoneId;
	private int temperature;
	private int humidity;
	private int motion;
	private int reserve;
	
	public static TagInputInfo getObject(String cacheValue) {
		if (StringUtils.isBlank(cacheValue)) {
			return null;
		}
 		String[] split = cacheValue.split(RedisUtils.DELIMETER);
 		TagInputInfo tag = new TagInputInfo();
 		tag.setTagId(split[0]);
 		tag.setRootOrgId(Long.parseLong(split[1]));
		tag.setSequenceNumber(Integer.parseInt(split[2]));
 		tag.setListenerId(Long.parseLong(split[3]));
		tag.setRssi(Integer.parseInt(split[4]));
 		tag.setTimestamp(Long.parseLong(split[5]));
		tag.setBattery(Float.parseFloat(split[6]));
		tag.setTamper(Integer.parseInt(split[7]));
 		tag.setPhoneId(Long.parseLong(split[8]));
		tag.setTemperature(Integer.parseInt(split[9]));
		tag.setHumidity(Integer.parseInt(split[10]));
		tag.setMotion(Integer.parseInt(split[11]));
		tag.setReserve(Integer.parseInt(split[12]));
 		return tag;
	}
}
