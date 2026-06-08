package tfg.funkomania.funkomania_api.dtos.notificacion_dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tfg.funkomania.funkomania_api.persistence.entities.VistaNotificacionesUsuarios;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoNotificacionEnum;
import tfg.funkomania.funkomania_api.persistence.enums.TipoNotificacionEnum;

/**
 * <p>DTO que representa una vista de notificaciones en el sistema de Funkomania con ID.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
public class VistaNotificacionUsuarioDTOId {
    @NotNull(message = "El identificador de la notificación no puede ser nulo")
    private Long idNotificacion;

    @NotNull(message = "El identificador del usuario no puede ser nulo")
    private Long idUsuario;

    @NotNull(message = "El tipo de notificación no puede ser nulo")
    private TipoNotificacionEnum tipoNotificacion;

    @NotNull(message = "El estado de la notificación no puede ser nulo")
    private EstadoNotificacionEnum estadoNotificacion;

    @NotBlank(message = "El mensaje de la notificación no puede ser nulo")
    private String mensaje;

    public VistaNotificacionUsuarioDTOId(VistaNotificacionesUsuarios vistaNotificacionesUsuarios) {
        this.idUsuario = vistaNotificacionesUsuarios.getIdUsuario();
        this.idNotificacion = vistaNotificacionesUsuarios.getIdUsuario();
        this.tipoNotificacion = vistaNotificacionesUsuarios.getTipoNotificacion();
        this.estadoNotificacion = vistaNotificacionesUsuarios.getEstadoNotificacion();
        this.mensaje = vistaNotificacionesUsuarios.getMensaje();
    }
}
