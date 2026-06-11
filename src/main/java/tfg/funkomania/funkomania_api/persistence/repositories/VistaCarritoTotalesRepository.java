package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.funkomania.funkomania_api.persistence.entities.VistaCarritoTotales;

import java.util.List;

/**
 * Interfaz de repositorio para la entidad VistaCarritoTotales.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public interface VistaCarritoTotalesRepository extends JpaRepository<VistaCarritoTotales, Long> {
    List<VistaCarritoTotales> findByIdCarrito(Long idCarrito);
}
