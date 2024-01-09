package com.track.alerts.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j(topic = "alert")
public class WebClientService<T> {


    @Autowired
    private WebClient webClient;

 public void post(T payload , String url) {

       String resp = webClient.post()
               .uri(url)
               .accept(MediaType.APPLICATION_JSON)
               .contentType(MediaType.APPLICATION_JSON)
               .body(Mono.just(payload),payload.getClass())
               .retrieve()
               .bodyToMono(String.class).block();
 }


}
