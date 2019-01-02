package com.mywuwu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MywuwuGatewayApplication {
    private static final Logger logger = LoggerFactory.getLogger(MywuwuGatewayApplication.class);

//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route(r -> r.path("/baidu")
//                        .uri("http://baidu.com:80/")
//                )
//                .route("websocket_route", r -> r.path("/apitopic1/**")
//                        .uri("ws://127.0.0.1:6605"))
//                .route(r -> r.path("/mywuwu-ribbon/**")
//                        .filters(f -> f.addResponseHeader("X-AnotherHeader", "mywuwu-ribbon"))
//
//                        .uri("lb://mywuwu-ribbon/")
//                )
//                .build();
//    }

    public static void main(String[] args) {
        SpringApplication.run(MywuwuGatewayApplication.class, args);
        logger.info(" Start APIGatewayApplication Done");
    }

}

