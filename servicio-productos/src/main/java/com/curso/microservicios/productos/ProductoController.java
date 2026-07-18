package com.curso.microservicios.productos;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final List<Producto> catalogo = List.of(
            new Producto(1L, "Laptop", new BigDecimal("7500.00")),
            new Producto(2L, "Mouse", new BigDecimal("150.00")),
            new Producto(3L, "Teclado", new BigDecimal("350.00")));

    @GetMapping
    public List<Producto> listar() {
        return catalogo;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        Optional<Producto> producto = catalogo.stream()
                .filter(p -> p.id().equals(id))
                .findFirst();
        return producto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
