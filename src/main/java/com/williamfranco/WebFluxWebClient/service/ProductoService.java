package com.williamfranco.WebFluxWebClient.service;

import com.williamfranco.WebFluxWebClient.modelDTO.ProductoDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {
    Flux<ProductoDTO> getAllProducts();
    Mono<ProductoDTO> getProductById(String id);
    Mono<ProductoDTO> updateProductById(ProductoDTO productoDTO, String id);
    Mono<ProductoDTO> saveProduct(ProductoDTO productoDTO);
    Mono<Void> deleteProductById(String id);
}
