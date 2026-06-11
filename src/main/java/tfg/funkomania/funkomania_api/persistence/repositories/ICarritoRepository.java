package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tfg.funkomania.funkomania_api.persistence.entities.Carrito;

/**
 * Interfaz de repositorio para la entidad Carrito.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public interface ICarritoRepository extends JpaRepository<Carrito, Long> {
    @Query("UPDATE Carrito c SET c.fechaActualizacion = CURRENT_TIMESTAMP WHERE c.idCarrito = :idCarrito")
    boolean updateFechaActualizacionByIdCarrito(Long idCarrito);
}
