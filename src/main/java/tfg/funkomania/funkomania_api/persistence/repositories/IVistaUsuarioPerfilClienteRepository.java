package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.repository.Repository;
import tfg.funkomania.funkomania_api.persistence.entities.VistaUsuarioPerfilCliente;

import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad VistaUsuarioPerfilCliente.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.2
 * @since 0.4.0
 */
public interface IVistaUsuarioPerfilClienteRepository extends Repository<VistaUsuarioPerfilCliente, Long> {
    Optional<VistaUsuarioPerfilCliente> findByEmail(String email);
}
