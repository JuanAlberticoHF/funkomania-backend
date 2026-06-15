package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;

import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad Usuario.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.1.0
 */
public interface IUsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {
    boolean existsByEmail(String email);
    Optional<Usuario> findUsuarioByEmail(String email);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.productosDeseados WHERE u.idUsuario = :id")
    Optional<Usuario> findUsuarioByIdConListaDeseos(@Param("id") Long id);
}
