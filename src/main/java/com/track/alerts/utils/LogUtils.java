package com.track.alerts.utils;

import com.track.alerts.types.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class LogUtils {
    public static final Logger BATTERY = LoggerFactory.getLogger("batteryLog");

    public static final Logger TAMPER = LoggerFactory.getLogger("tamperLog");



    public static String getApplicationVersion() {
        return LogUtils.class.getPackage().getImplementationVersion();
    }
    
    public static void logBattery(String message , LogLevel logLevel) {
        switch (logLevel) {
            case TRACE :  BATTERY.trace(message);break;
            case DEBUG :  BATTERY.debug(message);break;
            case INFO :  BATTERY.info(message);break;
            case ERROR :  BATTERY.error(message);break;
            default:LOGGER.debug(message);
        }
    }

    public static void logTamper(String message , LogLevel logLevel) {
        switch (logLevel) {
            case TRACE :  TAMPER.trace(message);break;
            case DEBUG :  TAMPER.debug(message);break;
            case INFO :  TAMPER.info(message);break;
            case ERROR :  TAMPER.error(message);break;
            default:LOGGER.debug(message);
        }
    }

}
