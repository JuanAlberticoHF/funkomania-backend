package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.funkomania.funkomania_api.persistence.entities.VistaCarritoContenido;
import tfg.funkomania.funkomania_api.persistence.entities.VistaCarritoContenidoId;

import java.util.List;

/**
 * Interfaz de repositorio para la entidad VistaCarritoContenido.
 *
 * @author JuanAlbeticoHF
 * @version 1.1.0
 * @since 0.7.0
 */
public interface IVistaCarritoContenidoRepository extends JpaRepository<VistaCarritoContenido, VistaCarritoContenidoId> {
    List<VistaCarritoContenido> findVistaCarritoContenidosByIdUsuario(Long idUsuario);
}
