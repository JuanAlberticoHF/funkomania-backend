package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.funkomania.funkomania_api.persistence.entities.Producto;

/**
 * Interfaz de repositorio para la entidad Producto.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.2.0
 */
public interface IProductoRepository extends JpaRepository<Producto, Long> {
}
