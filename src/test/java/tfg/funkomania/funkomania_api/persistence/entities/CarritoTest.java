package tfg.funkomania.funkomania_api.persistence.entities;

import org.junit.jupiter.api.Test;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoCarritoEnum;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class CarritoTest {

    @Test
    void testCarritoCreation() {
        LocalDateTime now = LocalDateTime.now();
        Carrito carrito = Carrito.builder()
                .idCarrito(1L)
                .fechaCreacion(now)
                .fechaActualizacion(now)
                .estado(EstadoCarritoEnum.ACTIVO)
                .build();

        assertThat(carrito.getIdCarrito()).isEqualTo(1L);
        assertThat(carrito.getEstado()).isEqualTo(EstadoCarritoEnum.ACTIVO);
        assertThat(carrito.getFechaCreacion()).isEqualTo(now);
    }
}
