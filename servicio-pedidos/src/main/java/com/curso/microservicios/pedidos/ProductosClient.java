package com.curso.microservicios.pedidos;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

/**
 * Cliente HTTP hacia servicio-productos, protegido con Resilience4j.
 * Si el servicio de productos falla repetidamente, el circuit breaker
 * se abre y las llamadas van directo al método de fallback sin esperar.
 */
@Service
public class ProductosClient {

    private final RestClient restClient;

    public ProductosClient(RestClient.Builder builder) {
        // "servicio-productos" es el nombre registrado en Eureka
        this.restClient = builder.baseUrl("http://servicio-productos").build();
    }

    @CircuitBreaker(name = "productos", fallbackMethod = "catalogoFallback")
    public List<ProductoDto> obtenerCatalogo() {
        return restClient.get()
                .uri("/api/productos")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProductoDto>>() {
                });
    }

    /**
     * Fallback: respuesta degradada cuando productos no está disponible.
     * Debe tener la misma firma + el parámetro Throwable.
     */
    @SuppressWarnings("unused")
    private List<ProductoDto> catalogoFallback(Throwable causa) {
        return List.of();
    }
}
