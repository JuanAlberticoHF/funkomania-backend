package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.funkomania.funkomania_api.persistence.entities.VistaNotificacionesUsuarios;

import java.util.List;

/**
 * Interfaz de repositorio para la entidad VistaNotificacionesUsuarios.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
public interface IVistaNotificacionesUsuarioRepository extends JpaRepository<VistaNotificacionesUsuarios, Long> {
    List<VistaNotificacionesUsuarios> findVistaNotificacionesUsuariosByIdUsuario(Long idUsuario);
}
