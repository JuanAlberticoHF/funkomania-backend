package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import tfg.funkomania.funkomania_api.dtos.direccion_dtos.DireccionDTO;

/**
 * <p>Entidad que representa una dirección en el sistema de Funkomania.</p>
 * <p>La entidad mapea tabla {@code Dirección} de la base de datos</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "Direccion")
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDireccion", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    @Size(max = 120, message = "La calle no debe exceder los 120 caracteres.")
    @NotBlank(message = "La calle no debe estar vacía.")
    @Column(name = "Calle", nullable = false)
    private String calle;

    @Size(max = 10, message = "El numero no debe exceder los 10 caracteres.")
    @NotBlank(message = "La numero no puede ser nulo o estar vacío.")
    @Column(name = "Numero", nullable = false)
    private String numero;

    @Size(max = 10, message = "El piso no debe exceder los 10 caracteres.")
    @Column(name = "Piso")
    private String piso;

    @Size(max = 10, message = "La puerta no debe exceder los 10 caracteres.")
    @Column(name = "Puerta")
    private String puerta;

    @Size(max = 100, message = "La ciudad no debe exceder los 100 caracteres.")
    @NotBlank(message = "La ciudad no debe ser nula o estar vacía.")
    @Column(name = "Ciudad", nullable = false)
    private String ciudad;

    @Size(max = 100, message = "El municipio no debe exceder los 100 caracteres.")
    @NotBlank(message = "El municipio no debe ser nulo o estar vacío.")
    @Column(name = "Municipio", nullable = false)
    private String municipio;

    @Size(max = 100, message = "La provincia no debe exceder los 100 caracteres.")
    @NotBlank(message = "La provincia no puede ser nula o estar vacía.")
    @Column(name = "Provincia", nullable = false)
    private String provincia;

    @Size(max = 10, message = "El código postal no debe exceder los 10 caracteres.")
    @NotBlank(message = "El código postal no puede ser nulo o estar vacío.")
    @Column(name = "CP", nullable = false)
    private String codigoPostal;

    @NotNull(message = "El campo 'activo' no puede ser nulo")
    @Column(name = "Activo", nullable = false)
    private Boolean activo;

    public Direccion(DireccionDTO direccionDTO) {
        this.calle = direccionDTO.getCalle();
        this.numero = direccionDTO.getNumero();
        this.piso = direccionDTO.getPiso();
        this.puerta = direccionDTO.getPuerta();
        this.ciudad = direccionDTO.getCiudad();
        this.municipio = direccionDTO.getMunicipio();
        this.provincia = direccionDTO.getProvincia();
        this.codigoPostal = direccionDTO.getCodigoPostal();
        this.activo = direccionDTO.getActivo();
    }
}
