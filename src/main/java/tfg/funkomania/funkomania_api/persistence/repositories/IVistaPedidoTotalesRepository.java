package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.funkomania.funkomania_api.persistence.entities.VistaDetallePedidoId;
import tfg.funkomania.funkomania_api.persistence.entities.VistaPedidoTotales;

/**
 * Interfaz de repositorio para la entidad {@link VistaPedidoTotales}.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public interface IVistaPedidoTotalesRepository extends JpaRepository<VistaPedidoTotales, VistaDetallePedidoId> {
    VistaPedidoTotales findByIdPedidoAndIdUsuario(Long idPedido, Long idUsuario);
    VistaPedidoTotales findByIdPedido(Long idPedido);
}
