package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tfg.funkomania.funkomania_api.persistence.entities.Direccion;

import java.util.List;

/**
 * Interfaz de repositorio para la entidad {@link Direccion}.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
public interface IDireccionRepository extends JpaRepository<Direccion, Long> {
    @Modifying
    @Query(value = "CALL sp_activar_direccion_usuario(:p_idUsuario, :p_idDireccion)", nativeQuery = true)
    void activarDireccion(@Param("p_idDireccion") Long idDireccion, @Param("p_idUsuario") Long idUsuario);

    List<Direccion> findDireccionsByUsuario_Id(Long usuarioId);

    boolean existsDireccionById(Long id);
}
