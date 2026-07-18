package com.curso.microservicios.productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservicio de productos: expone un catálogo en memoria.
 * Se registra en Eureka con el nombre SERVICIO-PRODUCTOS.
 */
@SpringBootApplication
public class ProductosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductosApplication.class, args);
    }
}
