package tfg.funkomania.funkomania_api.persistence.repositories;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tfg.funkomania.funkomania_api.persistence.entities.VistaPedidosAdmin;

import java.util.List;

/**
 * Interfaz de repositorio para la entidad {@link VistaPedidosAdmin}.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public interface IVistaPedidosAdminRepository extends JpaRepository<VistaPedidosAdmin, Long>, JpaSpecificationExecutor<VistaPedidosAdmin> {
    @NullMarked
    List<VistaPedidosAdmin> findAll(Specification<VistaPedidosAdmin> spec);
}
