package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.funkomania.funkomania_api.persistence.entities.VistaCarritoTotales;

/**
 * Interfaz de repositorio para la entidad VistaCarritoTotales.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.2
 * @since 0.7.0
 */
public interface IVistaCarritoTotalesRepository extends JpaRepository<VistaCarritoTotales, Long> {
    VistaCarritoTotales findByIdCarrito(Long idCarrito);
}
