package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoNotificacionEnum;
import tfg.funkomania.funkomania_api.persistence.enums.TipoNotificacionEnum;

/**
 * <p>Entidad que representa una notificación en el sistema de Funkomania.</p>
 * <p>La entidad mapea tabla {@code Notificacion} de la base de datos</p>
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
@Table(name = "Notificacion")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idNotificacion", nullable = false)
    private Long idNotificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @NotNull(message = "El tipo de notificación no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoNotificacionEnum tipoNotificacion;

    @NotNull(message = "El estado de la notificación no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoNotificacionEnum estadoNotificacion;
}
