package com.curso.microservicios.pedidos;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final ProductosClient productosClient;

    public PedidoController(ProductosClient productosClient) {
        this.productosClient = productosClient;
    }

    /**
     * Simula la creación de un pedido: consulta el catálogo remoto.
     * Si productos está caído, el fallback devuelve una lista vacía
     * y el pedido queda marcado como "catálogo no disponible".
     */
    @GetMapping("/nuevo")
    public Map<String, Object> nuevoPedido() {
        List<ProductoDto> catalogo = productosClient.obtenerCatalogo();
        String estado = catalogo.isEmpty()
                ? "PENDIENTE: catálogo no disponible (fallback activado)"
                : "CREADO";
        return Map.of(
                "estado", estado,
                "productosDisponibles", catalogo);
    }
}
