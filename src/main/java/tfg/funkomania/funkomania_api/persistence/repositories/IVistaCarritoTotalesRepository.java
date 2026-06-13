package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.funkomania.funkomania_api.persistence.entities.VistaCarritoTotales;

import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad VistaCarritoTotales.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.3
 * @since 0.7.0
 */
public interface IVistaCarritoTotalesRepository extends JpaRepository<VistaCarritoTotales, Long> {
    Optional<VistaCarritoTotales> findByIdCarrito(Long idCarrito);
}
