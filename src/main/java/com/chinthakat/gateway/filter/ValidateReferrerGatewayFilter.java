package com.chinthakat.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ValidateReferrerGatewayFilter extends AbstractGatewayFilterFactory<ValidateReferrerGatewayFilter.Config> {

    public ValidateReferrerGatewayFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
            builder.header("gateway-timestamp", "" + System.currentTimeMillis());
            builder.header("access-control-url", config.getAccessControlUrl());

            //return chain.filter(exchange.mutate().request(builder.build()).build());
            WebClient webClient = WebClient.create();

            return webClient.post()
                    .uri(config.getAccessControlUrl())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just("test message payload"),String.class) //properly set access control message body and header here
                    .retrieve()
                    .bodyToMono(String.class).flatMap(response -> {
                        System.out.println(response);
                        if("SUCCESS".equals(response)) {
                            builder.header("access-control-result", "successful"); //properly proses access control response and set value here
                        } else {
                            builder.header("access-control-result", "failed");
                        }
                        return chain.filter(exchange.mutate().request(builder.build()).build());
                    });

        };
    }

    public static class Config {
        private String accessControlUrl;

        public Config(){
            accessControlUrl = "http://localhost:8082/access/validate";
        }

        public String getAccessControlUrl() {
            return accessControlUrl;
        }

        public void setAccessControlUrl(String accessControlUrl) {
            this.accessControlUrl = accessControlUrl;
        }
    }
}
