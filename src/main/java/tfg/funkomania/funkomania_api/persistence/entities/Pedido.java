package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPagoEnum;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPedidoEnum;

import java.time.LocalDateTime;

/**
 * <p>Entidad que representa un pedido en el sistema de Funkomania.</p>
 * <p>La entidad mapea tabla {@code pedido} de la base de datos</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Pedido")
public class Pedido {
    @Positive(message = "El ID del pedido debe ser un número positivo.")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPedido", nullable = false)
    private Long idPedido;

    @NotNull(message = "El código del pedido no puede ser nulo.")
    @Size(max = 30, message = "El código del pedido no puede exceder los 30 caracteres.")
    @Column(name = "CodigoPedido", nullable = false, unique = true)
    private String codigoPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    @NotNull(message = "La fecha del pedido no puede ser nula.")
    @PastOrPresent(message = "La fecha del pedido no puede ser futura.")
    @Column(name = "FechaPedido", nullable = false)
    private LocalDateTime fechaPedido;

    @NotNull(message = "El estado del pedido no puede ser nulo.")
    @Column(name = "EstadoPedido", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPedidoEnum estadoPedido;

    @NotNull(message = "El estado del pago no puede ser nulo.")
    @Column(name = "EstadoPago", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPagoEnum estadoPago;

    @NotNull(message = "La dirección del pedido no puede ser nula.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDireccion", nullable = false)
    @EqualsAndHashCode.Exclude
    private Direccion direccion;

    @NotNull(message = "El método de pago del pedido no puede ser nulo.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idMetodoPago", nullable = false)
    @EqualsAndHashCode.Exclude
    private MetodoPago metodoPago;

    @Lob
    @Column(name = "Comentarios", columnDefinition = "TEXT")
    private String comentarios;

    @NotNull(message = "La fecha de ultima modificación no puede ser nula.")
    @PastOrPresent(message = "La fecha de ultima modificación no puede ser futura.")
    @Column(name = "UltimaModif", nullable = false)
    private LocalDateTime ultimaModificacion;
}
