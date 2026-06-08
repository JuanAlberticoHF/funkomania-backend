package tfg.funkomania.funkomania_api.dtos.categoria_dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * <p>DTO que representa un categoria sin su id en el sistema de Funkomania.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.6.0
 */
public record CategoriaDTORequest (
        @NotBlank(message = "El nombre de la categoría no debe estar vacío.")
        @Size(max = 50, message = "El nombre de la categoría no debe exceder los 50 caracteres.")
        String nombre,
        @Positive(message = "El ID de la categoría padre debe ser un número positivo.")
        Long idCategoriaPadre) {}
