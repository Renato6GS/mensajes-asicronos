package com.curso.microservicios.pedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

/**
 * Microservicio de pedidos: consulta el catálogo de productos
 * a través de Eureka y se protege con un Circuit Breaker.
 */
@SpringBootApplication
public class PedidosApplication {

    public static void main(String[] args) {
        SpringApplication.run(PedidosApplication.class, args);
    }

    /**
     * @LoadBalanced permite usar el nombre lógico de Eureka
     * (http://servicio-productos) en lugar de una IP fija.
     */
    @Bean
    @LoadBalanced
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
