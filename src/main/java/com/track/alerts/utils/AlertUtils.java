package com.track.alerts.utils;

import com.track.alerts.Models.AlertCountCheck;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "alert")
public class AlertUtils {

    public static final long DEFAULT_ALERT_DELAY = 60L;

    public static final int DEFAULT_ALERT_STATUS = 1;

    public static final int ALERT_STATUS = 1;

    public static final int BATTERY_SUBSCRIPTION_PRIORITY = 11;

    public static final int TAMPER_SUBSCRIPTION_PRIORITY = 15;


    public static boolean isAlertable(AlertCountCheck alertCountCheck, int sequenceNo) {

        if (alertCountCheck.getSequenceNo() < sequenceNo) {
            alertCountCheck.setSequenceNo(sequenceNo);
            if (alertCountCheck.getCount() >= 3) {
                return true;
            }
            alertCountCheck.setCount(alertCountCheck.getCount() + 1);
        }
        return false;
    }
}
