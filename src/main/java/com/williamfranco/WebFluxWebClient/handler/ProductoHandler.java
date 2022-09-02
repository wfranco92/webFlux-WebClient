package com.williamfranco.WebFluxWebClient.handler;

import com.williamfranco.WebFluxWebClient.modelDTO.ProductoDTO;
import com.williamfranco.WebFluxWebClient.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ProductoHandler {

    @Autowired
    private ProductoService productoService;

    public Mono<ServerResponse> getAllProducts(ServerRequest serverRequest){
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(productoService.getAllProducts(), ProductoDTO.class);
    }

    public Mono<ServerResponse> getProductById(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productoService.getProductById(id), ProductoDTO.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

/*
    // metodo con flatmap para llamar primero al service.
    public Mono<ServerResponse> getProductById(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");
        return productoService.getProductById(id)
                .flatMap(producto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(producto))
                .switchIfEmpty(ServerResponse.notFound().build());
    }*/


}
