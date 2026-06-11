package tfg.funkomania.funkomania_api.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Clase que representa la clave primaria compuesta de la entidad {@link VistaCarritoContenido}.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class VistaCarritoContenidoId implements Serializable {
    private Long idUsuario;
    private Long idCarrito;
    private Long idProducto;
}
