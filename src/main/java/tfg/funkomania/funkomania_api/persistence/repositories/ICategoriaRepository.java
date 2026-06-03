package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.funkomania.funkomania_api.persistence.entities.Categoria;

/**
 * Interfaz de repositorio para la entidad Categoria.
 *
 * @author JuanAlbeticoHF
 * @version 0.2.0
 * @since 0.2.0
 */
public interface ICategoriaRepository extends JpaRepository<Categoria,Long> {
}
