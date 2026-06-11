package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoCarritoEnum;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Entidad que representa un carrito de compras en el sistema de Funkomania.</p>
 * <p>La entidad mapea tabla {@code Carrito} de la base de datos</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.1
 * @since 0.7.0
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Carrito")
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarrito;

    @OneToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL)
    private Set<DetalleCarrito> detalleCarrito = new HashSet<>();

    @NotNull(message = "La fecha de creación del carrito no puede ser nula.")
    @PastOrPresent(message = "La fecha de creación del carrito no puede ser futura.")
    @Column(name = "FechaCreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @NotNull(message = "La fecha de actualización del carrito no puede ser nulo.")
    @PastOrPresent(message = "La fecha de actualización del carrito no puede ser futura.")
    @Column(name = "FechaActualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @NotNull(message = "El estado del carrito no puede ser nulo.")
    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoCarritoEnum estado;
}
