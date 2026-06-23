package tfg.funkomania.funkomania_api.persistence.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pruebas unitarias para enumerators del sistema.
 *
 * @author JuanAlberticoHF
 * @since 1.0.0
 */
class EnumsTest {

    @Test
    void testEstadoCarritoEnum() {
        assertEquals("ACTIVO", EstadoCarritoEnum.ACTIVO.name());
        assertEquals("ABANDONADO", EstadoCarritoEnum.ABANDONADO.name());
    }

    @Test
    void testEstadoNotificacionEnum() {
        assertEquals("PENDIENTE", EstadoNotificacionEnum.PENDIENTE.name());
        assertEquals("ENVIADA", EstadoNotificacionEnum.ENVIADA.name());
        assertEquals("LEIDA", EstadoNotificacionEnum.LEIDA.name());
    }

    @Test
    void testEstadoPagoEnum() {
        assertEquals("PENDIENTE", EstadoPagoEnum.PENDIENTE.name());
        assertEquals("PAGADO", EstadoPagoEnum.PAGADO.name());
        assertEquals("RECHAZADO", EstadoPagoEnum.RECHAZADO.name());
    }

    @Test
    void testEstadoPedidoEnum() {
        assertEquals("PENDIENTE", EstadoPedidoEnum.PENDIENTE.name());
        assertEquals("ENVIADO", EstadoPedidoEnum.ENVIADO.name());
        assertEquals("ENTREGADO", EstadoPedidoEnum.ENTREGADO.name());
        assertEquals("CANCELADO", EstadoPedidoEnum.CANCELADO.name());
    }

    @Test
    void testRoleEnum() {
        assertEquals("CLIENTE", RoleEnum.CLIENTE.name());
        assertEquals("ADMIN", RoleEnum.ADMIN.name());
    }

    @Test
    void testTipoNotificacionEnum() {
        assertEquals("REGISTRO", TipoNotificacionEnum.REGISTRO.name());
        assertEquals("COMPRA", TipoNotificacionEnum.COMPRA.name());
        assertEquals("ESTADO_PEDIDO", TipoNotificacionEnum.ESTADO_PEDIDO.name());
        assertEquals("CARRITO_ABANDONADO", TipoNotificacionEnum.CARRITO_ABANDONADO.name());
        assertEquals("PAGO_ERROR", TipoNotificacionEnum.PAGO_ERROR.name());
        assertEquals("LISTADESEOS_STOCK", TipoNotificacionEnum.LISTADESEOS_STOCK.name());
        assertEquals("BIENVENIDA", TipoNotificacionEnum.BIENVENIDA.name());
    }
}
