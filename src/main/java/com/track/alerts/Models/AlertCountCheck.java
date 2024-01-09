package com.track.alerts.Models;

import com.track.alerts.utils.RedisUtils;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlertCountCheck {

    private int alertType;
    private int count;
    private int sequenceNo;
    private long timestamp;

    public static AlertCountCheck getObject(String value) {
        if(StringUtils.isBlank(value) ) {
            return null;
        }
        String[] valueArray = value.split(RedisUtils.DELIMETER);
        if(valueArray.length >= 4) {
            return AlertCountCheck.builder()
                    .alertType(Integer.parseInt(valueArray[0]))
                    .count(Integer.parseInt(valueArray[1]))
                    .sequenceNo(Integer.parseInt(valueArray[2]))
                    .timestamp(Long.parseLong(valueArray[3]))
                    .build();
        } else {
            return null;
        }
    }
}
