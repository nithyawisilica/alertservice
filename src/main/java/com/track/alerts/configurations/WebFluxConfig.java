package com.track.alerts.configurations;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@EnableWebFlux
@Slf4j(topic = "webConnection")
public class WebFluxConfig implements WebFluxConfigurer {

    @Value("${notification.server}")
    private String notificationServer;

    @Value("${web.client.http.client.con.timeout}")
    private Integer connectionTimeout;

    @Value("${web.client.http.client.read.timeout}")
    private Integer readTimeout;

    @Value("${web.client.http.client.write.timeout}")
    private Integer writeTimeout;


    @Bean
    public WebClient getWebClient() {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(c -> c.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
                        .doOnConnected(con -> con.addHandler(new ReadTimeoutHandler(readTimeout))
                                                 .addHandler(new WriteTimeoutHandler(writeTimeout))
                        )
                );

        return WebClient.builder()
                .baseUrl(notificationServer)
                .clientConnector(new ReactorClientHttpConnector(httpClient.wiretap(true)))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


}
