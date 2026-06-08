package tfg.funkomania.funkomania_api.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tfg.funkomania.funkomania_api.dtos.notificacion_dtos.VistaNotificacionUsuarioDTOId;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.*;
import tfg.funkomania.funkomania_api.persistence.entities.Notificacion;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoNotificacionEnum;
import tfg.funkomania.funkomania_api.persistence.enums.TipoNotificacionEnum;
import tfg.funkomania.funkomania_api.persistence.repositories.INotificacionRepository;
import tfg.funkomania.funkomania_api.persistence.repositories.IUsuarioRepository;
import tfg.funkomania.funkomania_api.persistence.repositories.IVistaNotificacionesUsuarioRepository;

import java.util.List;
import java.util.Objects;

/**
 * <p>Servicio para gestionar las notificaciones de un usuario de Funkomania.</p>
 * <p>Esta clase implementa la interfaz {@link NotificacionService} y proporciona la lógica de negocio para las operaciones
 * relacionadas con las notificaciones del usuario.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.1
 * @since 0.5.0
 */
@Service
public class NotificacionServiceImpl implements NotificacionService {

    /** Repositorio para gestionar notificaciones */
    private final INotificacionRepository notificacionRepository;

    /** Repositorio para acceder a la vista de notificaciones de usuarios en la base de datos */
    private final IVistaNotificacionesUsuarioRepository vistaNotificacionesUsuarioRepository;

    /** Repositorio para gestionar usuarios */
    private final IUsuarioRepository usuarioRepository;

    public NotificacionServiceImpl(INotificacionRepository notificacionRepository,
                                   IVistaNotificacionesUsuarioRepository vistaNotificacionesUsuarioRepository,
                                   IUsuarioRepository usuarioRepository) {
        this.notificacionRepository = notificacionRepository;
        this.vistaNotificacionesUsuarioRepository = vistaNotificacionesUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<VistaNotificacionUsuarioDTOId> obtenerTodasLasNotificacionesDelUsuario() {
        // Obtenemos el email del usuario autenticado desde el contexto de seguridad de Spring Security
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Validamos que el email no sea nulo antes de continuar con la actualización del usuario
        if (email == null) throw new NullEmailAutenticationException(
                "El email obtenido es nulo por problemas de autenticación, no se puede actualizar el usuario autenticado");

        // Buscamos el usuario en la base de datos utilizando el email obtenido del contexto de seguridad
        Long idUsuario = usuarioRepository.findUsuarioByEmail(email)
                .orElseThrow(() -> new UsuarioNotFoundException(
                        "No se encontró un usuario con el email del usuario autenticado: " + email)).getIdUsuario();

        // Buscamos las notificaciones del usuario en la base de datos utilizando el id del usuario obtenido
        return vistaNotificacionesUsuarioRepository.findVistaNotificacionesUsuariosByIdUsuario(idUsuario)
                .stream().map(VistaNotificacionUsuarioDTOId::new).toList();
    }

    @Override
    public void leerNotificacion(Long idNotificacion) {
        // Obtenemos el email del usuario autenticado desde el contexto de seguridad de Spring Security
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Validamos que el email no sea nulo antes de continuar con la actualización del usuario
        if (email == null) throw new NullEmailAutenticationException(
                "El email obtenido es nulo por problemas de autenticación, no se puede actualizar el usuario autenticado");

        // Buscamos el usuario en la base de datos utilizando el email obtenido del contexto de seguridad
        Long idUsuario = usuarioRepository.findUsuarioByEmail(email)
                .orElseThrow(() -> new UsuarioNotFoundException(
                        "No se encontró un usuario con el email del usuario autenticado: " + email)).getIdUsuario();

        // Buscamos la notificación en la base de datos utilizando el identificador de la notificación.
        Notificacion notificacion = notificacionRepository.findById(idNotificacion).orElseThrow(
                () -> new NotificacionNotFoundException("La notificación con identificador" + idNotificacion + " no existe en la base de datos"));

        // Validamos que el usuario autenticado sea el propietario de la notificación antes de cambiar su estado a "LEIDO"
        if (!Objects.equals(notificacion.getUsuario().getIdUsuario(), idUsuario))
            throw new NotNotificationOwnerException(
                "La notificación con identificador" + idNotificacion + " no pertenece al usuario autenticado");

        // Validamos que la notificación no este ya leída antes de cambiar su estado a "LEIDO"
        if (notificacion.getEstadoNotificacion() == EstadoNotificacionEnum.LEIDA)
            throw new NotificacionYaLeidaException(
                "La notificación con identificador" + idNotificacion + " ya ha sido leída");


        // Si existe la notificación y el usuario autenticado es propietaria cambiamos su estado a "LEIDO"
        notificacionRepository.findByIdNotificacionAndEstadoNotificacion(idNotificacion, EstadoNotificacionEnum.LEIDA);
    }

    @Override
    public void generarNotificacion(Long idUsuario, TipoNotificacionEnum tipoNotificacion) {
        // Buscamos el usuario en la base de datos utilizando el id del usuario obtenido
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(
                () -> new UsuarioNotFoundException("No se encontró un usuario con el identificador: " + idUsuario));

        // Construimos la notificación
        Notificacion notificacion = Notificacion.builder()
                .idNotificacion(null)
                .usuario(usuario)
                .tipoNotificacion(tipoNotificacion)
                .estadoNotificacion(EstadoNotificacionEnum.ENVIADA).build();

        // Guardamos la notificación en la base de datos
        notificacionRepository.save(notificacion);
    }
}
