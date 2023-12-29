package com.track.alerts;

import com.track.alerts.services.BatteryService;
import com.track.alerts.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

	@Scheduled(fixedRate = 1000)
	public void startBatteryAlert() throws InterruptedException {
		 batteryService.sendBatteryAlert();
	}


	@Scheduled(fixedRate = 1000)
	public void startTamperAlert(){
		//Thread.currentThread().setName("Tamper");
		//System.out.println("Tamper  " +Thread.currentThread().getName());
//		System.out.println("Tamper  " + Thread.currentThread().getName()+System.currentTimeMillis());
		//sortedCache.execute(RedisConnectionCommands::ping);
	}

}
