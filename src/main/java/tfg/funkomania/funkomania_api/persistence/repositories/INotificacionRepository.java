package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.funkomania.funkomania_api.persistence.entities.Notificacion;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoNotificacionEnum;

/**
 * Interfaz de repositorio para la entidad Notificacion.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
public interface INotificacionRepository extends JpaRepository<Notificacion, Long> {
    void findByIdNotificacionAndEstadoNotificacion(Long idNotificacion, EstadoNotificacionEnum estado);
}
