package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * <p>Clase que representa la clave primaria compuesta de la entidad {@link DetallePedido}.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class DetallePedidoId {
    @NotNull(message = "El ID del pedido no puede ser nulo.")
    @Column(name = "idPedido")
    private Long idCarrito;

    @NotNull(message = "El ID del producto no puede ser nulo.")
    @Column(name = "idProducto")
    private Long idProducto;
}
