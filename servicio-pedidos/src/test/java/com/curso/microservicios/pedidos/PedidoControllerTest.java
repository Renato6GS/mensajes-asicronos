package com.curso.microservicios.pedidos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

class PedidoControllerTest {

    private final ProductosClient productosClient = mock(ProductosClient.class);
    private final PedidoController controller = new PedidoController(productosClient);

    @Test
    void pedidoSeCreaCuandoHayCatalogo() {
        when(productosClient.obtenerCatalogo())
                .thenReturn(List.of(new ProductoDto(1L, "Laptop", new BigDecimal("7500.00"))));

        var respuesta = controller.nuevoPedido();

        assertThat(respuesta.get("estado")).isEqualTo("CREADO");
    }

    @Test
    void pedidoQuedaPendienteCuandoElFallbackDevuelveVacio() {
        when(productosClient.obtenerCatalogo()).thenReturn(List.of());

        var respuesta = controller.nuevoPedido();

        assertThat(respuesta.get("estado").toString()).startsWith("PENDIENTE");
    }
}
