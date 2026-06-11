package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.funkomania.funkomania_api.persistence.entities.VistaCarritoContenido;

import java.util.List;

/**
 * Interfaz de repositorio para la entidad VistaCarritoContenido.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public interface VistaCarritoContenidoRepository extends JpaRepository<VistaCarritoContenido, Long> {
    List<VistaCarritoContenido> findVistaCarritoContenidoByIdUsuario(Long idUsuario);
}
