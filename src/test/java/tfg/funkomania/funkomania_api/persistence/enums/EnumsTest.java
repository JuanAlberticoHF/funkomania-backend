package tfg.funkomania.funkomania_api.persistence.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumsTest {

    @Test
    void testEstadoCarritoEnum() {
        assertEquals("ACTIVO", EstadoCarritoEnum.ACTIVO.name());
        assertEquals("ABANDONADO", EstadoCarritoEnum.ABANDONADO.name());
    }

    @Test
    void testEstadoPedidoEnum() {
        assertEquals("PENDIENTE", EstadoPedidoEnum.PENDIENTE.name());
        assertEquals("ENVIADO", EstadoPedidoEnum.ENVIADO.name());
        assertEquals("ENTREGADO", EstadoPedidoEnum.ENTREGADO.name());
        assertEquals("CANCELADO", EstadoPedidoEnum.CANCELADO.name());
    }
}
