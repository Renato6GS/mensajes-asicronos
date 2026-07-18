package com.curso.microservicios.productos;

import java.math.BigDecimal;

public record Producto(Long id, String nombre, BigDecimal precio) {
}
