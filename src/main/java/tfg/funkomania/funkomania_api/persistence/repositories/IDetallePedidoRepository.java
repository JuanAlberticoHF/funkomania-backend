package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.funkomania.funkomania_api.persistence.entities.DetallePedido;
import tfg.funkomania.funkomania_api.persistence.entities.DetallePedidoId;

/**
 * Interfaz de repositorio para la entidad DetallePedido.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public interface IDetallePedidoRepository extends JpaRepository<DetallePedido, DetallePedidoId> {
}
