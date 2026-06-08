package tfg.funkomania.funkomania_api.services;

import tfg.funkomania.funkomania_api.dtos.notificacion_dtos.VistaNotificacionUsuarioDTOId;
import tfg.funkomania.funkomania_api.persistence.entities.VistaNotificacionesUsuarios;
import tfg.funkomania.funkomania_api.persistence.enums.TipoNotificacionEnum;

import java.util.List;

/**
 * Interfaz de servicio de la entidad Notificacion y la vista VistaNotificacionesUsuarios
 * Define los métodos para realizar operaciones relacionadas con las notificaciones del usuario.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
public interface NotificacionService {
    /**
     * Obtiene todas las notificaciones del usuario autenticado.
     * @return Lista de notificaciones del usuario autenticado.
     */
    List<VistaNotificacionUsuarioDTOId> obtenerTodasLasNotificacionesDelUsuario();

    /**
     * Obtiene todas las notificaciones del usuario autenticado.
     * @param idNotificacion ID de la notificación a marcar como leída.
     */
    void leerNotificacion(Long idNotificacion);

    /**
     * Genera una nueva notificación para el usuario autenticado.
     * @param idUsuario ID del usuario para el cual se generará la notificación.
     * @param tipoNotificacion Tipo de notificación a generar.
     */
    void generarNotificacion(Long idUsuario, TipoNotificacionEnum tipoNotificacion);
}
