package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;

import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad Usuario.
 *
 * @author JuanAlbeticoHF
 * @version 0.2.0
 * @since 0.1.0
 */
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    Optional<Usuario> findUsuarioByEmail(String email);
}
