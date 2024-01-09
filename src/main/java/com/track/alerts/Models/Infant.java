package com.track.alerts.Models;

import com.wisilica.safeall.utils.RedisUtils;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Infant {

    long tagId;
    long infantId;
    String fName;
    String lName;

    long infantLayerId;

    public static Infant getObject(String value) {
        if (null != value) {
            String[] valueArray = value.split(RedisUtils.DELIMETER);
            if (valueArray.length >= 5) {
                return Infant.builder()
                        .tagId(Long.parseLong(valueArray[0]))
                        .infantId(Long.parseLong(valueArray[1]))
                        .fName(valueArray[2])
                        .lName(valueArray[3])
                        .infantLayerId(Long.parseLong(valueArray[4]))
                        .build();
            }
        }
        return new Infant( 0L, 0L,"No value in infant cache","No value in infant cache", 0L);
    }

}
