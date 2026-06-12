package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.funkomania.funkomania_api.persistence.entities.VistaHistorialPedidosUsuario;
import tfg.funkomania.funkomania_api.persistence.entities.VistaHistorialPedidosUsuarioId;

import java.util.List;

/**
 * Interfaz de repositorio para la entidad {@link VistaHistorialPedidosUsuario}.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public interface IVistaHistorialPedidosUsuarioRepository extends JpaRepository<VistaHistorialPedidosUsuario, VistaHistorialPedidosUsuarioId> {
    List<VistaHistorialPedidosUsuario> findByIdUsuario(Long idUsuario);
}
