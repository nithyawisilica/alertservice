package com.track.alerts.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Jobin Jacob Paily <jobinjp@wisilica.com>
 *
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AlertTypeEnum {

    NO_ALERT_TYPE(0, "NO_ALERT_TYPE"),
    BATTERY_ALERT_LEVEL(0 , "BATTERY_ALERT_LEVEL"),

    TAMPER_ALERT_LEVEL(1, "TAMPER_ALERT_LEVEL"),
    PANIC_ALERT_LEVEL(2,"PANIC_ALERT_LEVEL"),

    BATTERY_ALERT_TYPE(3, "BATTERY_ALERT_TYPE"),
    TAMPER_ALERT_TYPE(4,"TAMPER_ALERT_TYPE"),
    TAMPER_AND_BATTERY_ALERT_TYPE(7,"TAMPER_AND_BATTERY_ALERT_TYPE"),
    PANIC_ALERT_TYPE(14, "PANIC_ALERT_TYPE"),


    ;



    private int value;
    private String name;

    public static AlertTypeEnum getByName(String name){
        for(AlertTypeEnum en : values()) {
            if(en.getName().equals(name)) {
                return en;
            }
        }
        return null;
    }

    public static AlertTypeEnum getByValue(int value) {
        for(AlertTypeEnum en : values()) {
            if(value == en.getValue()) {
                return en;
            }
        }
        return null;
    }

}
