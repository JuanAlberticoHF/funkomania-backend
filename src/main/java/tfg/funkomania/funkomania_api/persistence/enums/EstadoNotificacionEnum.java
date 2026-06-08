package tfg.funkomania.funkomania_api.persistence.enums;

/**
 * <p>Enum para definir el estado de una notificación en el sistema. Actualmente, se definen tres estados:</p>
 * <li>{@code PENDIENTE}: Indica que la notificación está pendiente de ser enviada o procesada.</li>
 * <li>{@code ENVIADA}: Indica que la notificación ha sido enviada al destinatario.</li>
 * <li>{@code LEIDA}: Indica que el destinatario ha leído la notificación.</li>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
public enum EstadoNotificacionEnum {
    PENDIENTE,
    ENVIADA,
    LEIDA
}
