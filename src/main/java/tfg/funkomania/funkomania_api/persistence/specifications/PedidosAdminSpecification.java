package tfg.funkomania.funkomania_api.persistence.specifications;

import org.springframework.data.jpa.domain.Specification;
import tfg.funkomania.funkomania_api.persistence.entities.VistaPedidosAdmin;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPagoEnum;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPedidoEnum;

import java.time.LocalDateTime;

/**
 * Clase de especificación para construir consultas dinámicas sobre la entidad {@link VistaPedidosAdmin}
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public class PedidosAdminSpecification {
    /**
     * Crea una especificación para filtrar vistas por email o nombre
     * @param usuario El texto a buscar en el email o nombre del usuario
     * @return Una especificación que filtra vistas por email o nombre
     */
    public static Specification<VistaPedidosAdmin> busquedaContiene(String usuario) {
        return (root, query, criteriaBuilder) -> {
            if (usuario == null || usuario.isEmpty()) return null; // Si no hay filtro, lo ignora
            // SQL: WHERE LOWER(email) LIKE '%texto%'
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("emailUsuario")), "%" + usuario.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nombreUsuario")), "%" + usuario.toLowerCase() + "%")
            );
        };
    }

    /**
     * Crea una especificación para filtrar por ID de pedido
     * @param idPedido El ID del pedido a filtrar
     * @return Una especificación que filtra por ID de pedido
     */
    public static Specification<VistaPedidosAdmin> busquedaPorIdPedido(Long idPedido) {
        return (root, query, criteriaBuilder) -> idPedido == null ? null : criteriaBuilder.equal(root.get("idPedido"), idPedido);
    }

    /**
     * Crea una especificación para filtrar por Código de pedido
     * @param codigoPedido El código del pedido a filtrar
     * @return Una especificación que filtra por código de pedido
     */
    public static Specification<VistaPedidosAdmin> busquedaPorCodigoPedido(String codigoPedido) {
        return (root, query, criteriaBuilder) -> (codigoPedido == null || codigoPedido.isEmpty()) ? null : criteriaBuilder.equal(root.get("codigoPedido"), codigoPedido);
    }

    /**
     * Crea una especificación para filtrar por Fecha de pedido
     * @param fechaPedido La fecha del pedido a filtrar
     * @return Una especificación que filtra por fecha de pedido
     */
    public static Specification<VistaPedidosAdmin> busquedaPorFechaPedido(LocalDateTime fechaPedido) {
        return (root, query, criteriaBuilder) -> fechaPedido == null ? null : criteriaBuilder.equal(root.get("fechaPedido"), fechaPedido);
    }

    /**
     * Crea una especificación para filtrar por Estado de pedido
     * @param estadoPedido El estado del pedido a filtrar
     * @return Una especificación que filtra por estado de pedido
     */
    public static Specification<VistaPedidosAdmin> busquedaPorEstadoPedido(EstadoPedidoEnum estadoPedido) {
        return (root, query, criteriaBuilder) -> estadoPedido == null ? null : criteriaBuilder.equal(root.get("estadoPedido"), estadoPedido);
    }

    /**
     * Crea una especificación para filtrar por Estado de pago
     * @param estadoPago El estado de pago a filtrar
     * @return Una especificación que filtra por estado de pago
     */
    public static Specification<VistaPedidosAdmin> busquedaPorEstadoPago(EstadoPagoEnum estadoPago) {
        return (root, query, criteriaBuilder) -> estadoPago == null ? null : criteriaBuilder.equal(root.get("estadoPago"), estadoPago);
    }

    /**
     * Crea una especificación para filtrar por Metodo de pago
     * @param metodoPago El metodo de pago a filtrar
     * @return Una especificación que filtra por metodo de pago
     */
    public static Specification<VistaPedidosAdmin> busquedaPorMetodoPago(String metodoPago) {
        return (root, query, criteriaBuilder) -> (metodoPago == null || metodoPago.isEmpty()) ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("metodoPago")), "%" + metodoPago.toLowerCase() + "%");
    }
}
