package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tfg.funkomania.funkomania_api.persistence.entities.Producto;

/**
 * Interfaz de repositorio para la entidad Producto.
 *
 * @author JuanAlbeticoHF
 * @version 1.1.0
 * @since 0.2.0
 */
public interface IProductoRepository extends JpaRepository<Producto, Long> {
    @Modifying
    @Query("UPDATE Producto p SET p.activo = false WHERE p.id = :idProducto")
    void eliminarLogicamenteProductoByIdProducto(Long idProducto);

    boolean existsByIdAndActivoTrue(Long idProducto);
}
