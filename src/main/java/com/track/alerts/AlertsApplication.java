package com.track.alerts;

import com.track.alerts.services.BatteryService;
import com.track.alerts.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class AlertsApplication {

	@Autowired
	private BatteryService batteryService;

	@Autowired
	@Qualifier("sortedCache")
	StringRedisTemplate sortedCache;

	public static void main(String[] args) {
		SpringApplication.run(AlertsApplication.class, args);
		//LOGGER.info("Adaptor Version : {}", LogUtils.getApplicationVersion());
		//System.out.println("Adaptor Version : " + LogUtils.getApplicationVersion());
	}

	@EventListener(ApplicationReadyEvent.class)
	private void print() {
		LOGGER.info("Alert Service Version : {}" , LogUtils.getApplicationVersion());
		System.out.println("Alert Service Version : " + LogUtils.getApplicationVersion());
	}



}
