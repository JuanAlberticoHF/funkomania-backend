package tfg.funkomania.funkomania_api.dtos.direccion_dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * <p>DTO que representa una dirección en el sistema de Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.1
 * @since 0.5.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DireccionDTO {
    @Size(max = 120, message = "La calle no debe exceder los 120 caracteres.")
    @NotBlank(message = "La calle no debe estar vacía.")
    private String calle;

    @Size(max = 10, message = "El numero no debe exceder los 10 caracteres.")
    @NotBlank(message = "La numero no puede ser nulo o estar vacío.")
    private String numero;

    @Size(max = 10, message = "El piso no debe exceder los 10 caracteres.")
    private String piso;

    @Size(max = 10, message = "La puerta no debe exceder los 10 caracteres.")
    private String puerta;

    @Size(max = 100, message = "La ciudad no debe exceder los 100 caracteres.")
    @NotBlank(message = "La ciudad no debe ser nula o estar vacía.")
    private String ciudad;

    @Size(max = 100, message = "El municipio no debe exceder los 100 caracteres.")
    @NotBlank(message = "El municipio no debe ser nulo o estar vacío.")
    private String municipio;

    @Size(max = 100, message = "La provincia no debe exceder los 100 caracteres.")
    @NotBlank(message = "La provincia no puede ser nula o estar vacía.")
    private String provincia;

    @Size(max = 10, message = "El código postal no debe exceder los 10 caracteres.")
    @NotBlank(message = "El código postal no puede ser nulo o estar vacío.")
    private String codigoPostal;

    @NotNull(message = "El campo 'activo' no puede ser nulo")
    private Boolean activo;

}
