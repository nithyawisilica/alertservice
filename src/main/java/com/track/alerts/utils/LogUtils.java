package com.track.alerts.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtils {


    public static String getApplicationVersion() {
        return LogUtils.class.getPackage().getImplementationVersion();
    }

}
