package com.williamfranco.WebFluxWebClient.service;

import com.williamfranco.WebFluxWebClient.modelDTO.ProductoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductoServiceImplements implements ProductoService{

    @Autowired
    private WebClient client;

    @Override
    public Flux<ProductoDTO> getAllProducts() {
        return client.get()
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(ProductoDTO.class));  // se de utilizar retrive el cual maneja la solicitud de forma "automatica"
    }

    @Override
    public Mono<ProductoDTO> getProductById(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return client.get()
                .uri("/{id}", params)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProductoDTO.class);
                //.exchangeToMono(clientResponse -> clientResponse.bodyToMono(ProductoDTO.class));
    }

    @Override
    public Mono<ProductoDTO> updateProductById(ProductoDTO productoDTO, String id) {
        return client.put()
                .uri("/{id}", Collections.singletonMap("id", id))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                //.body(BodyInserters.fromValue(productoDTO))  // puede usar cualquier body aca descrito
                .bodyValue(productoDTO)
                //.body(Mono.just(productoDTO), ProductoDTO.class)
                .retrieve()
                .bodyToMono(ProductoDTO.class);
    }

    @Override
    public Mono<ProductoDTO> saveProduct(ProductoDTO productoDTO) {
        return client.post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                //.body(BodyInserters.fromValue(productoDTO))  // puede usar cualquier body aca descrito
                .bodyValue(productoDTO)
                //.body(Mono.just(productoDTO), ProductoDTO.class)
                .retrieve()
                .bodyToMono(ProductoDTO.class);
    }

    @Override
    public Mono<Void> deleteProductById(String id) {
        return client.delete()
                .uri("/{id}", Collections.singletonMap("id", id))
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(ClientResponse::releaseBody)
                .then();
    }
}
