package com.williamfranco.WebFluxWebClient.handler;

import com.williamfranco.WebFluxWebClient.modelDTO.ProductoDTO;
import com.williamfranco.WebFluxWebClient.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class ProductoHandler {

    @Autowired
    private ProductoService productoService;

    public Mono<ServerResponse> getAllProducts(ServerRequest serverRequest){
        return ServerResponse.ok().contentType(APPLICATION_JSON)
                .body(productoService.getAllProducts(), ProductoDTO.class)
                .onErrorResume(throwable -> {
                    WebClientResponseException error = (WebClientResponseException) throwable;
                    if(error.getStatusCode() == HttpStatus.BAD_REQUEST){
                        return  ServerResponse.badRequest()
                                .contentType(APPLICATION_JSON)
                                .bodyValue(error.getResponseBodyAsString());
                    }
                    else{
                        return Mono.error(error);
                    }
                });
    }

    public Mono<ServerResponse> getProductById(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(productoService.getProductById(id), ProductoDTO.class)
                .onErrorResume(throwable -> {
                    WebClientResponseException error = (WebClientResponseException) throwable;
                    if (error.getStatusCode() == HttpStatus.NOT_FOUND){
                        Map<String, Object> body= new HashMap<>();
                        body.put("Message", "No existe el producto con id: " + id);
                        body.put("TimesTap", new Date());
                        body.put("Status", error.getStatusCode().value());
                        return ServerResponse.status(HttpStatus.NOT_FOUND)
                                .bodyValue(body);
                    }
                    else {
                        return Mono.error(error);
                    }
                });
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

    public Mono<ServerResponse> saveProduct(ServerRequest serverRequest){

        Mono<ProductoDTO> producto = serverRequest.bodyToMono(ProductoDTO.class);

        return producto.flatMap(productoDTO -> productoService.saveProduct(productoDTO))
                .flatMap(productoDTO -> ServerResponse.created(URI.create("/api/client/".concat(productoDTO.getId())))
                        .contentType(APPLICATION_JSON)
                        .bodyValue(productoDTO))
                .onErrorResume(throwable -> {
                    WebClientResponseException error = (WebClientResponseException) throwable;
                    if(error.getStatusCode() == HttpStatus.BAD_REQUEST){
                        return  ServerResponse.badRequest()
                                .contentType(APPLICATION_JSON)
                                .bodyValue(error.getResponseBodyAsString());
                    }
                    else{
                        return Mono.error(error);
                    }
                });
    }

    public Mono<ServerResponse> updateProduct(ServerRequest serverRequest){

        Mono<ProductoDTO> producto = serverRequest.bodyToMono(ProductoDTO.class);
        String id  = serverRequest.pathVariable("id");

        return  producto.flatMap(productoDTO -> productoService.updateProductById(productoDTO, id))
                .flatMap(productoDTO -> ServerResponse.created(URI.create("/api/client/".concat(productoDTO.getId())))
                        .contentType(APPLICATION_JSON)
                        .bodyValue(productoDTO))
                .onErrorResume(throwable -> {
                    WebClientResponseException error = (WebClientResponseException) throwable;
                    if(error.getStatusCode() == HttpStatus.BAD_REQUEST){
                        return  ServerResponse.badRequest()
                                .contentType(APPLICATION_JSON)
                                .bodyValue(error.getResponseBodyAsString());
                    }
                    else{
                        return Mono.error(error);
                    }
                });
    }

/*

        // otro metodo con el manejo del service en el body


        public Mono<ServerResponse> updateProduct(ServerRequest serverRequest){

        Mono<ProductoDTO> producto = serverRequest.bodyToMono(ProductoDTO.class);
        String id  = serverRequest.pathVariable("id");

        return  producto.flatMap(productoDTO -> ServerResponse.created(URI.create("/api/client/".concat(id)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(productoService.updateProductById(productoDTO, id), ProductoDTO));
    }*/

    public Mono<ServerResponse> deleteProductById(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");

        return productoService.deleteProductById(id)
                .then(ServerResponse.noContent().build())
                .onErrorResume(throwable -> {
                    WebClientResponseException error = (WebClientResponseException) throwable;
                    if (error.getStatusCode() == HttpStatus.NOT_FOUND){
                        Map<String, Object> body= new HashMap<>();
                        body.put("Message", "No existe el producto a eliminar con id: " + id);
                        body.put("TimesTap", new Date());
                        body.put("Status", error.getStatusCode().value());
                        return ServerResponse.status(HttpStatus.NOT_FOUND)
                                .bodyValue(body);
                    }
                    else {
                        return Mono.error(error);
                    }
                });
    }

    public Mono<ServerResponse> upload(ServerRequest request){
        String id = request.pathVariable("id");

        return request.multipartData().map(multipart -> multipart.toSingleValueMap().get("file"))
                        .cast(FilePart.class)
                        .flatMap(file -> productoService.upload(file, id))
                        .flatMap(p -> ServerResponse.created(URI.create("/api/client/".concat(p.getId())))
                                .contentType(APPLICATION_JSON)
                                .bodyValue(p));
    }

}
