package tfg.funkomania.funkomania_api.dtos.carrito_dtos;

import org.junit.jupiter.api.Test;
import tfg.funkomania.funkomania_api.persistence.entities.VistaCarritoContenido;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

class VistaCarritoContenidoDTOIdTest {

    @Test
    void testConstructorFromEntity() {
        VistaCarritoContenido entity = VistaCarritoContenido.builder()
                .idUsuario(1L)
                .producto("Producto1")
                .precioOriginalSinIVA(BigDecimal.TEN)
                .build();

        VistaCarritoContenidoDTOId dto = new VistaCarritoContenidoDTOId(entity);

        assertThat(dto.getIdUsuario()).isEqualTo(1L);
        assertThat(dto.getProducto()).isEqualTo("Producto1");
        assertThat(dto.getPrecioOriginalSinIVA()).isEqualTo(BigDecimal.TEN);
    }
}
