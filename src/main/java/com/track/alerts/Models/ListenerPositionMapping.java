package com.track.alerts.Models;

import com.wisilica.safeall.utils.RedisUtils;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListenerPositionMapping {

    private String listenerName;
    private float lattitude;
    private float longitude;
    private int layerId;
    private long mapLongId;

    public static ListenerPositionMapping getObject(String cacheValue) {
        if (StringUtils.isBlank(cacheValue)) {
            return null;
        }
        String[] split = cacheValue.split(RedisUtils.DELIMETER);
        return ListenerPositionMapping.builder()
                .listenerName(split[0])
                .lattitude(Float.parseFloat(split[1]))
                .longitude(Float.parseFloat(split[2]))
                .layerId(Integer.parseInt(split[3]))
                .mapLongId(Long.parseLong(split[4]))
                .build();

    }

}
