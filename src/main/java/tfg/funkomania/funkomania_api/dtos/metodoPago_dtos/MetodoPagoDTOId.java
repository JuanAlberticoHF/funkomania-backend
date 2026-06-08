package tfg.funkomania.funkomania_api.dtos.metodoPago_dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import tfg.funkomania.funkomania_api.persistence.entities.MetodoPago;

/**
 * <p>DTO que representa un metodo de pago con su id en el sistema de Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MetodoPagoDTOId {
    private Long id;

    @Size(max = 50, message = "El nombre del método de pago no debe exceder los 50 caracteres.")
    private String nombre;

    @NotNull(message = "El campo 'activo' no puede ser nulo")
    private Boolean activo;

    public MetodoPagoDTOId(MetodoPago metodoPago) {
        this.id = metodoPago.getId();
        this.nombre = metodoPago.getNombre();
        this.activo = metodoPago.getActivo();
    }
}