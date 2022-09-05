package com.williamfranco.WebFluxWebClient;

import com.williamfranco.WebFluxWebClient.handler.ProductoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(ProductoHandler productoHandler){
        return route(GET("/api/client"), productoHandler::getAllProducts)
                .andRoute(GET("/api/client/{id}"), productoHandler::getProductById)
                .andRoute(POST("/api/client"), productoHandler::saveProduct)
                .andRoute(PUT("/api/client/{id}"), productoHandler::updateProduct)
                .andRoute(DELETE("/api/client/{id}"), productoHandler::deleteProductById);
    }
}
