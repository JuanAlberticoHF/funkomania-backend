package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.funkomania.funkomania_api.persistence.entities.VistaDetallePedido;
import tfg.funkomania.funkomania_api.persistence.entities.VistaDetallePedidoId;

import java.util.List;

/**
 * Interfaz de repositorio para la entidad {@link VistaDetallePedido}.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public interface IVistaDetallePedidoRepository extends JpaRepository<VistaDetallePedido, VistaDetallePedidoId> {
    List<VistaDetallePedido> findByIdPedido(Long idPedido);
}
