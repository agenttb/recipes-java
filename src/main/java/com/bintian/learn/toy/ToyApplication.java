package com.bintian.learn.toy;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

public class ToyApplication {
    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.create()
                .proxy( proxy -> proxy
                        .type(ProxyProvider.Proxy.SOCKS5)
                        .host("127.0.0.1")
                        .port(7890)
                );
        var webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        Mono<String> response = webClient.get()
                .uri("https://google.com")
                .retrieve()
                .bodyToMono(String.class);
        response.blockOptional().ifPresent(System.out::println);
    }
}
