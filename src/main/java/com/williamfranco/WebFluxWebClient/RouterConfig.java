package com.williamfranco.WebFluxWebClient;

import com.williamfranco.WebFluxWebClient.handler.ProductoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(ProductoHandler productoHandler){
        return route(GET("/api/client"), productoHandler::getAllProducts)
                .andRoute(GET("/api/client/{id}"), productoHandler::getProductById);
    }
}
