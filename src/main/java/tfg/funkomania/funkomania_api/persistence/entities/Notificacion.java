package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoNotificacionEnum;
import tfg.funkomania.funkomania_api.persistence.enums.TipoNotificacionEnum;

/**
 * <p>Entidad que representa una notificación en el sistema de Funkomania.</p>
 * <p>La entidad mapea tabla {@code Notificacion} de la base de datos</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.1.0
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
    @Column(name = "idNotificacion", nullable = false)
    private Long idNotificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoNotificacionEnum tipoNotificacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoNotificacionEnum estadoNotificacion;
}
