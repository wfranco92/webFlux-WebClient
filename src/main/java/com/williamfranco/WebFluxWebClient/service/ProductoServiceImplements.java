package com.williamfranco.WebFluxWebClient.service;

import com.williamfranco.WebFluxWebClient.modelDTO.ProductoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

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
                .retrieve()
                .bodyToMono(Void.class);
    }

    @Override
    public Mono<ProductoDTO> upload(FilePart file, String id) {
        MultipartBodyBuilder parts = new MultipartBodyBuilder();
        parts.asyncPart("file", file.content(), DataBuffer.class).headers(h -> {
            h.setContentDispositionFormData("file", file.filename());
        });

        return client.post()
                .uri("/upload/{id}", Collections.singletonMap("id", id))
                .contentType(MULTIPART_FORM_DATA)
                .syncBody(parts.build())
                .retrieve()
                .bodyToMono(ProductoDTO.class);
    }
}
