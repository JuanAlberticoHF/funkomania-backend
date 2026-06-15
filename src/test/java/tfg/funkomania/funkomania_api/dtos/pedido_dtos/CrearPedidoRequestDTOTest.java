package tfg.funkomania.funkomania_api.dtos.pedido_dtos;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CrearPedidoRequestDTOTest {
    @Test
    void testRecord() {
        CrearPedidoRequestDTO dto = new CrearPedidoRequestDTO(1L, 1L, "Ninguno");
        assertThat(dto.idDireccion()).isEqualTo(1L);
        assertThat(dto.comentarios()).isEqualTo("Ninguno");
    }
}
