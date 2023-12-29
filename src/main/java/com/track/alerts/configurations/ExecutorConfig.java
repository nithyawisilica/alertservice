package com.track.alerts.configurations;

import com.track.alerts.constants.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class ExecutorConfig {



    @Value("${track.alert.battery.threads:1}")
    private Integer batteryThreadCount;
    @Value("${ track.alert.battery.thread.pool.capacity:10000}")
    private Integer batteryThreadQueueCapacity;
    @Value("${track.alert.battery.thread.name.prefix:batteryThread-}")
    private String batteryThreadNamePrefix;

    @Value("${track.tamper.threads:1}")
    private Integer tamperThreadCount;
    @Value("${ track.tamper.thread.pool.capacity:10000}")
    private Integer tamperThreadQueueCapacity;
    @Value("${track.tamper.thread.name.prefix:tamperThread-}")
    private String tamperThreadNamePrefix;



    @Bean(name = Constants.BATTERY_TASK_EXECUTOR)
    public Executor batteryTaskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(batteryThreadCount);
        executor.setMaxPoolSize(batteryThreadCount);
        executor.setQueueCapacity(batteryThreadQueueCapacity);
        executor.setThreadNamePrefix(batteryThreadNamePrefix);
        executor.initialize();
        return executor;
    }
    @Bean(name = Constants.TAMPER_TASK_EXECUTOR)
    public TaskExecutor tamperTaskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(tamperThreadNamePrefix);
        executor.setCorePoolSize(tamperThreadCount);
        executor.setMaxPoolSize(tamperThreadCount);
        executor.setQueueCapacity(tamperThreadQueueCapacity);
        executor.initialize();
        return executor;
    }
}
