package com.curso.microservicios.pedidos;

import java.math.BigDecimal;

public record ProductoDto(Long id, String nombre, BigDecimal precio) {
}
