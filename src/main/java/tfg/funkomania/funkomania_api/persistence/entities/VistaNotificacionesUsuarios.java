package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoNotificacionEnum;
import tfg.funkomania.funkomania_api.persistence.enums.TipoNotificacionEnum;

/**
 * <p>Entidad que representa una vista de notificación en el sistema Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Immutable
@Table(name = "VNotificaciones_Usuarios")
public class VistaNotificacionesUsuarios {

    @Id
    @Column(name = "idNotificacion", nullable = false)
    private Long idNotificacion;

    @Column(name = "idUsuario", nullable = false)
    private Long idUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoNotificacionEnum tipoNotificacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoNotificacionEnum estadoNotificacion;

    @Column(name = "Mensaje")
    private String mensaje;
}
