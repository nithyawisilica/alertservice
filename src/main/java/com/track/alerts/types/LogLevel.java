package com.track.alerts.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogLevel {

    TRACE(0 ,"TRACE"),
    DEBUG(1 ,"DEBUG"),
    INFO(2 ,"INFO"),
    ERROR(3 ,"ERROR"),
    ;

    private final Integer code;
    private final String description;
}
