package com.track.alerts.Models;

import com.track.alerts.utils.RedisUtils;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Settings {

    int rootOrgId;
    int subOrgId;
    long value;
    int status;
    long lastChangedTimeStamp;

    public static Settings getObject(String value) {
        if(value != null) {
            String[] valueArray = value.split(RedisUtils.DELIMETER);
            if(valueArray.length >= 4) {
                return Settings.builder()
                        .rootOrgId(Integer.parseInt(valueArray[0]))
                        .subOrgId(Integer.parseInt(valueArray[1]))
                        .value(Long.parseLong(valueArray[2]))
                        .status(Integer.parseInt(valueArray[3]))
                        .build();
            }
        }
        return null;
    }
}
