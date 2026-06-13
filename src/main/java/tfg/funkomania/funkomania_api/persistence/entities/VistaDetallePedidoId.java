package tfg.funkomania.funkomania_api.persistence.entities;

import lombok.*;

/**
 * Clase que representa la clave primaria compuesta de la entidad {@link VistaDetallePedido}.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class VistaDetallePedidoId {
    private Long idPedido;
    private String codigoPedido;
    private Long idProducto;
}
