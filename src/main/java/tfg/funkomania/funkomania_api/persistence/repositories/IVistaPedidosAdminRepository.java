package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.funkomania.funkomania_api.persistence.entities.VistaPedidosAdmin;

/**
 * Interfaz de repositorio para la entidad {@link VistaPedidosAdmin}.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public interface IVistaPedidosAdminRepository extends JpaRepository<VistaPedidosAdmin, Long> {
}
