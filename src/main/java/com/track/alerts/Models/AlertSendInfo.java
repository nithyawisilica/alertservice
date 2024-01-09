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
public class AlertSendInfo {
	
	private long tagId;
	private int rootOrgId;
	private int subOrgId;

	public static AlertSendInfo getObject(String cacheValue) {
		if (StringUtils.isBlank(cacheValue)) {
			return null;
		}
 		String[] split = cacheValue.split(RedisUtils.DELIMETER);
 		AlertSendInfo alert = new AlertSendInfo();
		alert.setTagId(Long.parseLong(split[0]));
		alert.setRootOrgId(Integer.parseInt(split[1]));
		alert.setSubOrgId(Integer.parseInt(split[2]));
 		return alert;
	}
}
