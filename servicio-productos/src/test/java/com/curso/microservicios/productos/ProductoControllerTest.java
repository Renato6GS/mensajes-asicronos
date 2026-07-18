package com.curso.microservicios.productos;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ProductoControllerTest {

    private final ProductoController controller = new ProductoController();

    @Test
    void listarDevuelveCatalogoCompleto() {
        assertThat(controller.listar()).hasSize(3);
    }

    @Test
    void buscarPorIdExistenteDevuelveProducto() {
        var respuesta = controller.buscarPorId(1L);
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(respuesta.getBody().nombre()).isEqualTo("Laptop");
    }

    @Test
    void buscarPorIdInexistenteDevuelve404() {
        var respuesta = controller.buscarPorId(99L);
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
