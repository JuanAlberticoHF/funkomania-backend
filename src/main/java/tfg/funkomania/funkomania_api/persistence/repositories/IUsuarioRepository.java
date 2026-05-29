package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;

/**
 * Interfaz de repositorio para la entidad Usuario.
 *
 * @author JuanAlbeticoHF
 * @version 0.1
 * @since 0.1.0
 */
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
}
