package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>Clase que representa la clave compuesta para la entidad {@link DetalleCarrito}.</p>
 * <p>Esta clase se utiliza para mapear la relación entre un carrito y un producto específico dentro del carrito.</p>
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
public class DetalleCarritoId {

    @NotNull(message = "El ID del carrito no puede ser nulo.")
    @Column(name = "idCarrito")
    private Long idCarrito;

    @NotNull(message = "El ID del producto no puede ser nulo.")
    @Column(name = "idProducto")
    private Long idProducto;
}