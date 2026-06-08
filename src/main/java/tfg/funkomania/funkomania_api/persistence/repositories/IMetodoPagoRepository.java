package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.funkomania.funkomania_api.persistence.entities.MetodoPago;

import java.util.List;

/**
 * Interfaz de repositorio para la entidad MetodoPago.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
public interface IMetodoPagoRepository extends JpaRepository<MetodoPago, Integer> {
    List<MetodoPago> findMetodoPagosByActivoIsTrue();
}
