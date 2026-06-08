package tfg.funkomania.funkomania_api.dtos.categoria_dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import tfg.funkomania.funkomania_api.persistence.entities.Categoria;

/**
 * <p>DTO que representa un categoria con su id en el sistema de Funkomania, sin incluir la categoría padre.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.6.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CategoriaDTOIdNoCategoriaPadre {
    @Positive(message = "El ID de la categoría no puede ser nulo.")
    private Long id;

    @NotBlank(message = "El nombre de la categoría no debe estar vacío.")
    @Size(max = 50, message = "El nombre de la categoría no debe exceder los 50 caracteres.")
    private String nombre;


    public CategoriaDTOIdNoCategoriaPadre(Categoria categoria) {
        this.id = categoria.getId();
        this.nombre = categoria.getNombre();
    }
}
